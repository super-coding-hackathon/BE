package com.supercoding.hackathon01.controller;

import com.supercoding.hackathon01.dto.home.request.RegisterHomeRequest;
import com.supercoding.hackathon01.dto.home.request.ViewHomeListRequest;
import com.supercoding.hackathon01.dto.home.resposne.HomeDetailResponse;
import com.supercoding.hackathon01.dto.home.resposne.ViewPointListResponse;
import com.supercoding.hackathon01.dto.vo.Response;
import com.supercoding.hackathon01.error.CustomException;
import com.supercoding.hackathon01.error.domain.UserErrorCode;
import com.supercoding.hackathon01.security.Auth;
import com.supercoding.hackathon01.security.TokenProvider;
import com.supercoding.hackathon01.service.HomeService;
import com.supercoding.hackathon01.utils.ApiUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/home")
@Api(tags = "집 관련 API")
public class HomeController {

    private final HomeService homeService;

    @Auth
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "집 등록 API", description = "폼 데이터로 요청")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "multipart/form-data",
            schema = @Schema(implementation = MultipartFile.class)))
    public Response<Void> registerHome(@ModelAttribute RegisterHomeRequest homeRequest,
                                        @ApiParam(value = "이미지 파일 (선택)")
                                        @RequestPart(value = "imageFiles", required = false)
                                        List<MultipartFile> imageFiles,
                                        @ApiParam(value = "썸네일 이미지 파일 (선택)")
                                        @RequestPart(value = "thumbnailImage", required = false)
                                        MultipartFile thumbnailImage) {

        homeService.registerHome(homeRequest, imageFiles, thumbnailImage);

        return ApiUtils.success(HttpStatus.CREATED, null);
    }

    @GetMapping(value = "")
    @Operation(summary = "건물 리스트 조회", description = "건물 리스트 조회")
    public Response<List<ViewPointListResponse>> findHomeList(ViewHomeListRequest viewHomeListRequest) {
        return ApiUtils.success(HttpStatus.OK, homeService.getHomeList(viewHomeListRequest));
    }

    @GetMapping("/{home_id}")
    @Operation(summary = "건물 디테일 조회", description = "건물 디테일 조회")
    public Response<HomeDetailResponse> detailHome(@PathVariable("home_id") Long homeId, HttpServletRequest request) {
        Long userId = null;
        if(request.getHeader("Authorization") != null) {
            if (!request.getHeader("Authorization").startsWith("Bearer ")) {
                throw new CustomException(UserErrorCode.HANDLE_ACCESS_DENIED);
            }
            String token = request.getHeader("Authorization").split(" ")[1];

            userId = TokenProvider.getUserId(token);
        }
        return ApiUtils.success(HttpStatus.OK, homeService.detailHome(homeId, userId));
    }

    @Auth
    @DeleteMapping("/{home_id}")
    @Operation(summary = "등록한 건물 제거")
    public Response<Void> deleteHome(@PathVariable("home_id") Long homeId) {
        return ApiUtils.success(HttpStatus.OK, homeService.deleteHome(homeId));
    }

    @Auth
    @PutMapping(value = "/{home_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "multipart/form-data",
            schema = @Schema(implementation = MultipartFile.class)))
    @Operation(summary = "등록한 건물 수정")
    public Response<Void> updateHome(@PathVariable("home_id") Long homeId,
                                     @ModelAttribute RegisterHomeRequest homeRequest,
                                     @ApiParam(value = "이미지 파일 (선택)")
                                         @RequestPart(value = "imageFiles", required = false)
                                         List<MultipartFile> imageFiles,
                                     @ApiParam(value = "썸네일 이미지 파일 (선택)")
                                         @RequestPart(value = "thumbnailImage", required = false)
                                         MultipartFile thumbnailImage
                                     ) {
        return ApiUtils.success(HttpStatus.OK, homeService.updateHome(homeId, homeRequest, imageFiles, thumbnailImage));
    }


}
