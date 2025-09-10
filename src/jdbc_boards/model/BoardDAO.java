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
            int affected = pstmt.executeUpdate();
            boolean ok = affected > 0;
//            //생성된 PK를 Board객체에 반영
//            try (ResultSet rs = pstmt.getGeneratedKeys()) {
//                if (rs.next()) {
//                    int newbno = rs.getInt(1);
//                    board.setBno(newbno);
//                    boardList.add(board);
//                }
//            }
            return ok;


        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
   }

    /*** 전체 글 조회 (최신순으로 정렬)*/
    public List<Board> selectAll(){
        conn = DBUtil.getConnection();
        List<Board> boardList = new ArrayList<>();
        String sql = "SELECT bno, btitle, bcontent, bwriter, bdate FROM boardTable ORDER BY bno DESC";
        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
             ResultSet rs = pstmt.executeQuery();
             while(rs.next()){
                 Board board = new Board();
                 board.setBno(rs.getInt("bno"));
                 board.setBtitle(rs.getString("btitle"));
                 board.setBcontent(rs.getString("bcontent"));
                 board.setBwriter(rs.getString("bwriter"));
                 board.setBdate(rs.getTimestamp("bdate"));
                 boardList.add(board);
             }

        } catch (Exception e) {
             e.printStackTrace();
        }
        return boardList;
    }

    /** 글 한건 조회 */
    public Board selectOne(int bno){
        conn = DBUtil.getConnection();
        String sql = "select * from boardTable where bno = ?";
        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, bno);
            try(ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {
                    Board board = new Board();
                    board.setBno(rs.getInt("bno"));
                    board.setBtitle(rs.getString("btitle"));
                    board.setBcontent(rs.getString("bcontent"));
                    board.setBwriter(rs.getString("bwriter"));
                    board.setBdate(rs.getTimestamp("bdate"));
                    return board;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
// 글 수정 (제목, 내용만 수정)
   public boolean updateBoard(Board board) {
        conn = DBUtil.getConnection();
        String sql = "UPDATE boardTable SET btitle = ? , bcontent = ? WHERE bno = ? ";
        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, board.getBtitle());
            pstmt.setString(2, board.getBcontent());
            pstmt.setInt(3, board.getBno());
            int ack =  pstmt.executeUpdate();
            if (ack > 0)  return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
       return false;
   }

   // 글 삭제

    public boolean deleteBoard(int bno) {
        conn = DBUtil.getConnection();
        String sql = "DELETE FROM boardTable WHERE bno = ?";
        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bno);
            int ack = pstmt.executeUpdate();
            if (ack > 0)  return true;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
