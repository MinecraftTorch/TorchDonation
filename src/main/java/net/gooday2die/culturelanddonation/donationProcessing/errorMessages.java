package net.gooday2die.culturelanddonation.donationProcessing;

/**
 * A method that is for parsing error messages
 */
public class errorMessages {
    /**
     * This method returns restAPI error messages according to response code/
     * @param responseCode the error response code
     * @return String object that contains error reasons.
     */
    public static String restAPI(int responseCode){
        return switch (responseCode) {
            case 404 -> "API KEY 오류입니다. 서버 관리자에게 연락해주세요";
            case 405 -> "REST API 오류입니다. 서버 관리자에게 연락해주세요";
            case 406 -> "컬쳐랜드 로그인을 할 수 없습니다. 서버 관리자에게 연락해주세요";
            case 407 -> "이미 사용된 문화상품권 핀번호입니다.";
            case 408 -> "문화상품권 번호 형식이 잘못되었습니다.";
            case 409 -> "존재하지 않는 핀번호입니다.";
            default -> "알려지지 않은 에러입니다. 서버 관리자에게 연락해주세요";
        };
    }
}
