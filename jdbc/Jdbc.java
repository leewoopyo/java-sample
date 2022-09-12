package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class Jdbc  {

    private static Logger logger = Logger.getLogger("Jdbc");

    public static void main(String[] args) {
        
        String testCase = "case_2_1";

        //h2 jdbc 드라이버가 있는지 확인
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }

        //case_1
        //Try-with-resources
        //[참고] https://ryan-han.com/post/java/try_with_resources/
        if (testCase.startsWith("case_1")) {

            //메모리 기반 h2(메모리에 담기기 때문에 휘발성임)
            String url = "jdbc:h2:mem:test;MODE=MySQL;";

            try (
                Connection connection = DriverManager.getConnection(url,"sa","");
                Statement statement = connection.createStatement();
                ) {
                    connection.setAutoCommit(false);
                    
                    // #1.테이블을 생성 (h2db를 mem 타입으로 했기 때문에 매번 생성했던 테이블이 사라짐)
                    statement.execute("create table member("
                                            + "id int auto_increment,"
                                            + "username varchar(255) not null,"
                                            + "password varchar(255) not null,"
                                            + "primary key(id))"
                                     );
                    
                    // #2.테이블에 데이터 추가
                    //DML 관련 SQL은 commit, rollback 추가
                    try {
                        statement.executeUpdate("insert into member (username, password) values ('user001','1234')");
                        connection.commit();
                    } catch (SQLException se) {
                        connection.rollback();
                    }
                    
                    // #3.데이터 조회결과 ResultSet에 세팅
                    ResultSet resultSet = statement.executeQuery("select * from member");
    
                    // #4.결과 확인
                    while(resultSet.next()){

                        int id = resultSet.getInt("id");
                        String username = resultSet.getString("username");
                        String password = resultSet.getString("password");

                        if ("case_1_1".equals(testCase)) {
                            logger.info("##### case_1_1 #####");

                            // 1. 빌터 패턴으로 구현
                            Member member1 = new Member.MemberBuilder()
                                                .id(id)
                                                .username(username)
                                                .password(password)
                                                .build();
                                                
                            logger.info("Member1 : " + member1.toString());

                        } else if ("case_1_2".equals(testCase)) {
                            logger.info("##### case_1_2 #####");

                            // 2. ResultSet을 생성자의 매개 변수로 입력
                            Member member2 = new Member(resultSet); 
                            logger.info("Member2 : " + member2.toString());

                        } else if ("case_1_3".equals(testCase)) {
                            logger.info("##### case_1_3 #####");

                            //3. lombok @Builder annotation을 이용한 빌더 생성
                            MemberLombok member3 = new MemberLombok.MemberLombokBuilder()
                                                        .id(id)
                                                        .username(username)
                                                        .password(password)
                                                        .build();
        
                            logger.info("Member3 : " + member3.toString());

                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        } 

        //case_2
        if (testCase.startsWith("case_2")) {
            //파일 기반 h2  
            String url = "jdbc:h2:file:./jdbc/test;MODE=MySQL;";      

            Connection connection = null;
            Statement statement = null;

            try {
                connection = DriverManager.getConnection(url, "sa", "");
                statement = connection.createStatement();

                connection.setAutoCommit(false);
                    
                // #1.테이블을 생성 (h2db를 mem 타입으로 했기 때문에 매번 생성했던 테이블이 사라짐)
                // statement.execute("create table member("
                //                         + "id int auto_increment,"
                //                         + "username varchar(255) not null,"
                //                         + "password varchar(255) not null,"
                //                         + "primary key(id))"
                //                  );
                
                // #2.테이블에 데이터 추가
                //DML 관련 SQL은 commit, rollback 추가
                // try {
                //     statement.executeUpdate("insert into member (username, password) values ('user001','1234')");
                //     connection.commit();
                // } catch (SQLException se) {
                //     connection.rollback();
                // }
                
                // #3.데이터 조회결과 ResultSet에 세팅
                ResultSet resultSet = statement.executeQuery("select * from member");

                // #4.결과 확인
                while(resultSet.next()){

                    int id = resultSet.getInt("id");
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");

                    if ("case_2_1".equals(testCase)) {
                        logger.info("##### case_2_1 #####");

                        // 1. 빌터 패턴으로 구현
                        Member member1 = new Member.MemberBuilder()
                                            .id(id)
                                            .username(username)
                                            .password(password)
                                            .build();
                                            
                        logger.info("Member1 : " + member1.toString());

                    } else if ("case_2_2".equals(testCase)) {
                        logger.info("##### case_2_2 #####");

                        // 2. ResultSet을 생성자의 매개 변수로 입력
                        Member member2 = new Member(resultSet); 
                        logger.info("Member2 : " + member2.toString());

                    } else if ("case_2_3".equals(testCase)) {
                        logger.info("##### case_2_3 #####");

                        //3. lombok @Builder annotation을 이용한 빌더 생성
                        MemberLombok member3 = new MemberLombok.MemberLombokBuilder()
                                                    .id(id)
                                                    .username(username)
                                                    .password(password)
                                                    .build();
    
                        logger.info("Member3 : " + member3.toString());

                    }
                }
            } catch (SQLException se) {
                se.printStackTrace();
                try {
                    connection.rollback();
                } catch (SQLException se2) {
                    se2.printStackTrace();
                }
            } finally {
                try {
                    statement.close();
                    connection.close();
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        }
    }
}
