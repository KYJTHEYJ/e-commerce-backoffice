package e3i2.ecommerce_backoffice.domain.admin.controller;

import e3i2.ecommerce_backoffice.common.annotation.LoginSessionCheck;
import e3i2.ecommerce_backoffice.common.dto.response.DataResponse;
import e3i2.ecommerce_backoffice.common.dto.response.MessageResponse;
import e3i2.ecommerce_backoffice.common.dto.session.SessionAdmin;
import e3i2.ecommerce_backoffice.common.exception.ErrorEnum;
import e3i2.ecommerce_backoffice.common.exception.ServiceErrorException;
import e3i2.ecommerce_backoffice.common.util.pagination.ItemsWithPagination;
import e3i2.ecommerce_backoffice.domain.admin.dto.*;
import e3i2.ecommerce_backoffice.domain.admin.dto.SearchAdminDetailResponse;
import e3i2.ecommerce_backoffice.domain.admin.dto.UpdateAdminRequest;
import e3i2.ecommerce_backoffice.domain.admin.dto.UpdateAdminResponse;
import e3i2.ecommerce_backoffice.domain.admin.entity.AdminRole;
import e3i2.ecommerce_backoffice.domain.admin.entity.AdminStatus;
import e3i2.ecommerce_backoffice.domain.admin.service.AdminService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static e3i2.ecommerce_backoffice.common.util.Constants.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admins")
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/signUp")
    public ResponseEntity<DataResponse<SignUpResponse>> signUp(@Valid @RequestBody SignUpRequest request) {
        SignUpResponse response = adminService.signUp(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(DataResponse.success(HttpStatus.CREATED.name(), response));
    }

    @PutMapping("/{adminId}/accept")
    @LoginSessionCheck
    public ResponseEntity<DataResponse<AcceptAdminResponse>> acceptAdmin(
            @PathVariable Long adminId,
            @SessionAttribute(value = ADMIN_SESSION_NAME) SessionAdmin loginAdmin
    ) {
        AcceptAdminResponse response = adminService.acceptAdmin(adminId, loginAdmin);

        return ResponseEntity.status(HttpStatus.OK).body(DataResponse.success(HttpStatus.OK.name(), response));
    }

    @PutMapping("/{adminId}/deny")
    @LoginSessionCheck
    public ResponseEntity<DataResponse<DeniedAdminResponse>> denyAdmin(
            @PathVariable Long adminId,
            @Valid @RequestBody DeniedAdminRequest request,
            @SessionAttribute(value = ADMIN_SESSION_NAME) SessionAdmin loginAdmin
    ) {
        DeniedAdminResponse response = adminService.denyAdmin(adminId, loginAdmin, request);

        return ResponseEntity.status(HttpStatus.OK).body(DataResponse.success(HttpStatus.OK.name(), response));
    }

    @PostMapping("/login")
    public ResponseEntity<DataResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request,
            HttpSession session
    ) {
        if (session.getAttribute(ADMIN_SESSION_NAME) != null) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }

        SessionAdmin sessionAdmin = adminService.login(request);
        session.setAttribute(ADMIN_SESSION_NAME, sessionAdmin);

        LoginResponse response = LoginResponse.register(
                sessionAdmin.getAdminId(),
                sessionAdmin.getAdminName(),
                sessionAdmin.getEmail(),
                sessionAdmin.getPhone(),
                sessionAdmin.getRole(),
                sessionAdmin.getStatus(),
                sessionAdmin.getCreatedAd(),
                sessionAdmin.getAcceptedAt()
        );

        return ResponseEntity.status(HttpStatus.OK).body(DataResponse.success(HttpStatus.OK.name(), response));
    }

    @PostMapping("/logout")
    @LoginSessionCheck
    public ResponseEntity<MessageResponse<Void>> logout(
            @SessionAttribute(value = ADMIN_SESSION_NAME) SessionAdmin loginAdmin, HttpSession session) {
        if (loginAdmin == null) {
            throw new ServiceErrorException(ErrorEnum.ERR_LOGOUT_DUPLICATED);
        }

        session.invalidate();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                MessageResponse.success(HttpStatus.NO_CONTENT.name(), MSG_LOGOUT_MSG)
        );
    }

    @GetMapping
    @LoginSessionCheck
    public ResponseEntity<DataResponse<ItemsWithPagination<List<SearchAdminDetailResponse>>>> getAdminList(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "desc") String sortOrder,
            @RequestParam(required = false) AdminRole role,
            @RequestParam(required = false) AdminStatus status,
            @SessionAttribute(ADMIN_SESSION_NAME) SessionAdmin loginAdmin
    ) {
        ItemsWithPagination<List<SearchAdminDetailResponse>> response = adminService.getAdminList(
                keyword, page, size, sortBy, sortOrder, role, status, loginAdmin
        );

        return ResponseEntity.status(HttpStatus.OK).body(DataResponse.success("OK", response));
    }

    @GetMapping("/{adminId}")
    @LoginSessionCheck
    public ResponseEntity<DataResponse<SearchAdminDetailResponse>> getAdminDetail(
            @PathVariable Long adminId,
            @SessionAttribute(value = ADMIN_SESSION_NAME, required = false) SessionAdmin loginAdmin
    ) {
        SearchAdminDetailResponse response = adminService.getAdminDetail(adminId, loginAdmin);
        return ResponseEntity.status(HttpStatus.OK).body(DataResponse.success(HttpStatus.OK.name(), response));
    }

    @PutMapping("/{adminId}")
    @LoginSessionCheck
    public ResponseEntity<DataResponse<UpdateAdminResponse>> updateAdmin(
            @PathVariable Long adminId,
            @Valid @RequestBody UpdateAdminRequest request,
            @SessionAttribute(value = ADMIN_SESSION_NAME) SessionAdmin loginAdmin
    ) {
        UpdateAdminResponse response = adminService.updateAdmin(adminId, request, loginAdmin);

        return ResponseEntity.status(HttpStatus.OK).body(DataResponse.success(HttpStatus.OK.name(), response));
    }

    @DeleteMapping("/{adminId}")
    @LoginSessionCheck
    public ResponseEntity<MessageResponse<Void>> deleteAdmin(
            @PathVariable Long adminId,
            @SessionAttribute(value = ADMIN_SESSION_NAME) SessionAdmin loginAdmin
    ) {
        adminService.deleteAdmin(adminId, loginAdmin);

        return ResponseEntity.status(HttpStatus.OK).body(MessageResponse.success(HttpStatus.OK.name(), MSG_DELETE_ADMIN_ACCOUNT));
    }

    @GetMapping("/me")
    @LoginSessionCheck
    public ResponseEntity<DataResponse<GetMyProfileResponse>> getMyProfile(
            @SessionAttribute(value = ADMIN_SESSION_NAME, required = false) SessionAdmin loginAdmin
    ) {
        GetMyProfileResponse response = adminService.getMyProfile(loginAdmin.getAdminId());

        return ResponseEntity.status(HttpStatus.OK).body(DataResponse.success(HttpStatus.OK.name(), response));
    }

    @PutMapping("/me/profile")
    @LoginSessionCheck
    public ResponseEntity<DataResponse<UpdateMyProfileResponse>> updateMyProfile(
            @Valid @RequestBody UpdateMyProfileRequest request,
            @SessionAttribute(value = ADMIN_SESSION_NAME) SessionAdmin loginAdmin
    ) {
        UpdateMyProfileResponse response = adminService.updateMyProfile(request, loginAdmin.getAdminId());

        return ResponseEntity.status(HttpStatus.OK).body(DataResponse.success(HttpStatus.OK.name(), response));
    }

    @PutMapping("/me/password")
    @LoginSessionCheck
    public ResponseEntity<MessageResponse<Void>> changeMyPassword(
            @Valid @RequestBody ChangeMyPasswordRequest request,
            @SessionAttribute(value = ADMIN_SESSION_NAME) SessionAdmin loginAdmin
    ) {
        adminService.changeMyPassword(request, loginAdmin.getAdminId());

        return ResponseEntity.status(HttpStatus.OK).body(MessageResponse.success(HttpStatus.OK.name(), MSG_CHANGE_PASSWORD_SUCCESS));
    }

    @PutMapping("/{adminId}/role")
    @LoginSessionCheck
    public ResponseEntity<DataResponse<ChangeAdminRoleResponse>> changeAdminRole(
            @PathVariable Long adminId,
            @RequestBody ChangeAdminRoleRequest request,
            @SessionAttribute(value = ADMIN_SESSION_NAME) SessionAdmin loginAdmin
    ) {
        ChangeAdminRoleResponse response = adminService.changeAdminRole(request, adminId, loginAdmin);

        return ResponseEntity.status(HttpStatus.OK).body(DataResponse.success(HttpStatus.OK.name(), response));
    }

    @PutMapping("/{adminId}/status")
    @LoginSessionCheck
    public ResponseEntity<DataResponse<ChangeAdminStatusResponse>> changeAdminStatus(
            @PathVariable Long adminId,
            @Valid @RequestBody ChangeAdminStatusRequest request,
            @SessionAttribute(value = ADMIN_SESSION_NAME) SessionAdmin loginAdmin
    ) {
        ChangeAdminStatusResponse response = adminService.changeAdminStatus(request, adminId, loginAdmin);

        return ResponseEntity.status(HttpStatus.OK).body(DataResponse.success(HttpStatus.OK.name(), response));
    }
}
