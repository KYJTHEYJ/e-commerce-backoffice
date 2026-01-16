package e3i2.ecommerce_backoffice.common.util;

public class Constants {

    private Constants() {
    }

    public static final String ADMIN_SESSION_NAME = "loginAdmin";
    public static final String MSG_SERVER_ERROR_OCCUR = "오류가 발생하였습니다, 잠시 후 다시 시도 바랍니다";
    public static final String MSG_LOGOUT_MSG = "로그아웃 되었습니다";
    public static final String MSG_DELETE_ADMIN_ACCOUNT = "관리자가 삭제 되었습니다";
    public static final String MSG_DELETE_CUSTOMER_ACCOUNT = "고객이 삭제 되었습니다";
    public static final String MSG_CHANGE_PASSWORD_SUCCESS = "비밀번호가 성공적으로 변경 되었습니다";
    public static final String MSG_ADMIN_STATUS_NOT_WAIT = "해당 계정은 승인 대기 상태가 아닙니다";
    public static final String MSG_LOGOUT_DUPLICATED = "이미 로그아웃 된 상태입니다";
    public static final String MSG_ADMIN_ACCOUNT_DENY_SIZE_ERR = "거부 사유는 1~50자 이내여야 합니다";
    public static final String MSG_ADMIN_ACCOUNT_DENY_BLANK_ERR = "거부 사유는 필수입니다";
    public static final String MSG_ADMIN_ACCOUNT_ROLE_NULL_ERR = "관리자 역할은 필수입니다";
    public static final String MSG_ADMIN_ACCOUNT_STATUS_NULL_ERR = "관리자 상태는 필수입니다";
    public static final String MSG_CUSTOMER_ACCOUNT_STATUS_NULL_ERR = "고객 상태는 필수입니다";
    public static final String MSG_DATA_INSERT_FAIL = "데이터 등록에 실패하였습니다";
    public static final String MSG_PASSWORD_SIZE_ERR = "비밀번호는 8~20자리 이내여야 합니다";
    public static final String MSG_EMAIL_PATTERN_ERR = "이메일 형식이 유효하지 않습니다";
    public static final String MSG_NAME_BLANK_ERR = "이름은 필수입니다";
    public static final String MSG_EMAIL_BLANK_ERR = "이메일은 필수입니다";
    public static final String MSG_PHONE_PATTERN_ERR = "전화번호 형식이 유효하지 않습니다";
    public static final String MSG_PHONE_BLANK_ERR = "전화번호는 필수입니다";
    public static final String MSG_ADMIN_REQUEST_MESSAGE_BLANK_ERR = "지원 사유는 필수입니다";
    public static final String MSG_DUPLICATED_EMAIL = "이미 등록된 이메일 입니다";
    public static final String MSG_WRONG_EMAIL_PASSWORD = "이메일이나 비밀번호를 다시 확인해주세요";
    public static final String MSG_NOT_FOUND_CUSTOMER = "존재하지 않는 고객 입니다";
    public static final String MSG_WAIT_ADMIN_ACCOUNT_LOGIN = "해당 계정은 승인 대기 중 입니다";
    public static final String MSG_DENY_ADMIN_ACCOUNT_LOGIN = "해당 계정은 관리자 신청이 거부 되었습니다";
    public static final String MSG_SUSPEND_ADMIN_ACCOUNT_LOGIN = "해당 계정은 정지된 상태입니다";
    public static final String MSG_IN_ACT_ADMIN_ACCOUNT_LOGIN = "해당 계정은 비활성화된 상태입니다";
    public static final String MSG_UNAUTHORIZED_ACCOUNT_LOGIN = "정상적인 계정이 아닙니다";
    public static final String MSG_ONLY_SUPER_ADMIN_ACCESS = "슈퍼 관리자만 접근할 수 있습니다";
    public static final String MSG_NOT_DELETE_ADMIN_SELF = "슈퍼 관리자만 접근할 수 있습니다";
    public static final String MSG_NOT_FOUND_ADMIN = "존재하지 않는 관리자 입니다";
    public static final String MSG_SAME_OLD_PASSWORD = "새 비밀번호가 기존 비밀번호와 같습니다";
    public static final String MSG_NOT_MATCH_PASSWORD = "현재 비밀번호와 일치하지 않습니다";
    public static final String MSG_DUPLICATED_PHONE = "이미 등록된 전화번호 입니다";
    public static final String MSG_PRODUCT_NAME_BLANK_ERR = "상품의 이름은 필수입니다";
    public static final String MSG_PRODUCT_CATEGORY_NULL_ERR = "상품의 카테고리는 필수입니다";
    public static final String MSG_PRODUCT_PRICE_BLANK_ERR = "상품의 가격은 필수입니다";
    public static final String MSG_PRODUCT_PRICE_MINUS_ERR = "상품의 가격은 0원 이상이어야 합니다";
    public static final String MSG_PRODUCT_PRICE_MAX_ERR = "상품의 가격이 한도를 넘었습니다";
    public static final String MSG_PRODUCT_QUANTITY_MAX_ERR = "상품의 수량이 한도를 넘었습니다";
    public static final String MSG_PRODUCT_STATUS_NULL_ERR = "상품의 상태는 필수입니다";
    public static final String MSG_PRODUCT_QUANTITY_BLANK_ERR = "상품의 수량은 필수입니다";
    public static final String MSG_DELETE_PRODUCT = "상품이 삭제 되었습니다";
    public static final String MSG_NOT_FOUND_PRODUCT = "존재하지 않는 상품 입니다";
    public static final String MSG_NOT_LOGIN_ACCESS = "로그인 후 이용 가능합니다";
    public static final String MSG_DENY_CUSTOMER_ACCOUNT_DELETE = "주문 내역이 존재하여 삭제할 수 없습니다";
    public static final String MSG_NOT_VALID_VALUE = "유효하지 않은 값이 입력되었습니다";
    public static final String MSG_ORDER_CUSTOMER_ID_NULL_ERR = "주문 고객 아이디는 필수입니다";
    public static final String MSG_ORDER_PRODUCT_ID_NULL_ERR = "주문 상품 아이디는 필수입니다";
    public static final String MSG_ORDER_QUANTITY_NULL_ERR = "상품의 주문수량은 필수입니다";
    public static final String MSG_ORDER_QUANTITY_MAX_ERR = "상품의 주문수량이 한도를 넘었습니다";
    public static final String MSG_ORDER_QUANTITY_MIN_ERR = "상품의 주문수량은 1개 이상 이여야 합니다";
    public static final String MSG_ORDER_TO_DISCONTINUE_ERR = "단종된 상품은 주문할 수 없습니다";
    public static final String MSG_ORDER_TO_SOLD_OUT_ERR = "품절된 상품은 주문할 수 없습니다";
    public static final String MSG_ORDER_TO_QUANTITY_OVER_ERR = "주문 수량이 상품 재고보다 많습니다";
}


