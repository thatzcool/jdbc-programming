package jdbc_boards.model;

import jdbc_boards.vo.Board;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BoardDAO {
    private Connection conn;
    List<Board> boardList = new ArrayList<>();
    //글 등록기능
    public boolean createBoard(Board board) {
        //1. Connection 필요
        conn = DBUtil.getConnection();
        //2. 쿼리 생성
        String sql = " insert into boardTable(btitle,bcontent,bwriter,bdate) values(?,?,?,now())";
        //3. Connection 쿼리를 담아 DB서버로 request 할 객체 PrepareStatement 생성
        try (PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            //4. 값을 셋팅
            pstmt.setString(1, board.getBtitle());
            pstmt.setString(2, board.getBcontent());
            pstmt.setString(3, board.getBwriter());
            //5. 서버로 전송 후 결과값 (int 성공 1 실패 0)
            int affected =  pstmt.executeUpdate();
            boolean ok = affected > 0;
            //생성된 PK를 Board객체에 반영
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int newbno = rs.getInt(1);
                    board.setBno(newbno);
                    boardList.add(board);
                }
            }
            return ok;


        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }



    }
}
