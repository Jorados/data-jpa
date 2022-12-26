package study.datajpa.repository;

//구현체기반 Projections
public class UsernameOnlyDto {
    private final String username;

    //파라미터 명을 분석함.
    public UsernameOnlyDto(String username){
        this.username = username;
    }

    public String getUsername(){
        return username;
    }
}
