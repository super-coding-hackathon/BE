package com.supercoding.hackathon01.service;

import com.supercoding.hackathon01.dto.home.request.RegisterHomeRequest;
import com.supercoding.hackathon01.dto.home.request.ViewHomeListRequest;
import com.supercoding.hackathon01.dto.home.resposne.HomeDetailResponse;
import com.supercoding.hackathon01.dto.home.resposne.ViewPointListResponse;
import com.supercoding.hackathon01.entity.*;
import com.supercoding.hackathon01.enums.FilePath;
import com.supercoding.hackathon01.error.CustomException;
import com.supercoding.hackathon01.error.domain.FileErrorCode;
import com.supercoding.hackathon01.error.domain.HomeErrorCode;
import com.supercoding.hackathon01.error.domain.UserErrorCode;
import com.supercoding.hackathon01.repository.AddressRepository;
import com.supercoding.hackathon01.repository.CategoryRepository;
import com.supercoding.hackathon01.repository.PictureRepository;
import com.supercoding.hackathon01.repository.UserRepository;
import com.supercoding.hackathon01.repository.home.HomeCustomRepositoryImpl;
import com.supercoding.hackathon01.repository.home.HomeRepository;
import com.supercoding.hackathon01.security.AuthHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final AwsS3Service awsS3Service;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final HomeRepository homeRepository;
    private final HomeCustomRepositoryImpl homeCustomRepository;
    private final AddressRepository addressRepository;
    private final PictureRepository pictureRepository;

    @Transactional
    public void registerHome(RegisterHomeRequest homeRequest, List<MultipartFile> imageFiles, MultipartFile thumbnailImage) {

        User user = validUser(AuthHolder.getUserId());
        Category category = categoryRepository.findById(homeRequest.getCategoryId()).orElseThrow(() -> new CustomException(HomeErrorCode.NOT_FOUND_CATEGORY));
        Home home = Home.of(homeRequest, user, category);
        homeRepository.save(home);
        Address address = Address.of(homeRequest, home);
        addressRepository.save(address);
        List<Picture> pictures = new ArrayList<>();

        if (imageFiles != null) {

            List<Picture> originPicture = pictureRepository.findByHome(home);
            originPicture.forEach(picture -> {
                if (Boolean.FALSE.equals(picture.getIsThumbnail())) {
                    deletedFile(picture.getUrl());
                    pictureRepository.deleteById(picture.getId());
                }
            });

            imageFiles.forEach(file -> pictures.add(Picture.of(uploadImageFile(file, home), home, false)));
            pictureRepository.saveAll(pictures);
        }
        if (thumbnailImage != null) {

            List<Picture> originPicture = pictureRepository.findByHome(home);

            originPicture.forEach(picture -> {
                if (Boolean.TRUE.equals(picture.getIsThumbnail())) {
                    deletedFile(picture.getUrl());
                    pictureRepository.deleteById(picture.getId());
                }
            });
            Picture thumbnail = Picture.of(uploadImageFile(thumbnailImage, home), home, true);
            pictureRepository.save(thumbnail);
        }
    }

    @Transactional
    public Void updateHome(Long homeId, RegisterHomeRequest homeRequest, List<MultipartFile> imageFiles, MultipartFile thumbnailImage) {

        Home originHome = validHome(homeId);
        validRegisterUser(AuthHolder.getUserId(), originHome.getUser().getId());
        User user = validUser(AuthHolder.getUserId());
        Category category = categoryRepository.findById(homeRequest.getCategoryId()).orElseThrow(() -> new CustomException(HomeErrorCode.NOT_FOUND_CATEGORY));
        Home newHome = Home.of(homeRequest, user, category);
        newHome.setId(originHome.getId());

        homeRepository.save(newHome);

        Address originAddress = addressRepository.findByHome(newHome).orElseThrow(() -> new CustomException(HomeErrorCode.NOT_FOUND_HOME));
        Address address = Address.of(homeRequest, newHome);
        address.setId(originAddress.getId());
        addressRepository.save(address);

        List<Picture> removePictures = pictureRepository.findByHome(newHome);
        removePictures.forEach(picture -> awsS3Service.removeFile(picture.getUrl()));
        pictureRepository.deleteAll(removePictures);

        List<Picture> pictures = new ArrayList<>();

        if (imageFiles != null) {



            imageFiles.forEach(file -> pictures.add(Picture.of(uploadImageFile(file, newHome), newHome, false)));
            pictureRepository.saveAll(pictures);
        }
        if (thumbnailImage != null) {
            Picture thumbnail = Picture.of(uploadImageFile(thumbnailImage, newHome), newHome, true);
            pictureRepository.save(thumbnail);
        }

        return null;
    }

    public List<ViewPointListResponse> getHomeList(ViewHomeListRequest viewHomeListRequest) {
        return homeCustomRepository.findPointList(viewHomeListRequest);
    }

    public HomeDetailResponse detailHome(Long homeId, Long userId) {
        Home home = validHome(homeId);
        Address address = addressRepository.findByHome(home).orElseThrow(() -> new CustomException(HomeErrorCode.NOT_FOUND_HOME));
        boolean isMine = false;
        if (userId != null) {
            if (Objects.equals(home.getUser().getId(), userId)) isMine = true;
        }
        return HomeDetailResponse.of(home, address, isMine);
    }

    public Void deleteHome(Long homeId) {

        Home home = validHome(homeId);
        validRegisterUser(AuthHolder.getUserId(), home.getUser().getId());
        homeRepository.updateIsDeletedById(home.getId());

        return null;
    }

    private String uploadImageFile(MultipartFile multipartFile, Home home) {
        String uniqueIdentifier = UUID.randomUUID().toString();
        try {
            if (multipartFile != null) {
                return awsS3Service.uploadImage(multipartFile, FilePath.HOME_DIR.getPath() + home.getId() + "/" + uniqueIdentifier);
            }
        }catch (IOException e) {
            throw new CustomException(FileErrorCode.FILE_UPLOAD_FAILED);
        }
        return null;
    }

    private void deletedFile(String s3Url) {
        awsS3Service.removeFile(s3Url);
    }

    private User validUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new CustomException(UserErrorCode.NOT_FOUND_USER));
    }

    private Home validHome(Long homeId) {
        return homeRepository.findById(homeId).orElseThrow(() -> new CustomException(HomeErrorCode.NOT_FOUND_HOME));
    }

    private void validRegisterUser(Long userId, Long registerUserId) {
        if (!Objects.equals(userId, registerUserId)) {
            throw new CustomException(HomeErrorCode.FORBIDDEN_HOME);
        }
    }
}
