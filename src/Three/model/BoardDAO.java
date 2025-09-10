package Three.model;

import Four.vo.Board;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//3조  Table명 : boardTable3
public class BoardDAO {
    private Connection conn;
    List<Board> boardList = new ArrayList<>();

    //글 등록기능
    public boolean createBoard(Board board) {
        String sql = "INSERT INTO boardtable3 (btitle,bcontent,bwriter,bdate) VALUES(?,?,?,now())";
        conn = DBUtil.getConnection();
        try(
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ){
            pstmt.setString(1, board.getBtitle());
            pstmt.setString(2, board.getBcontent());
            pstmt.setString(3, board.getBwriter());

            int result = pstmt.executeUpdate();
            if(result == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
   }

    /*** 전체 글 조회 (최신순으로 정렬)*/
    public List<Board> selectAll(){
        conn = DBUtil.getConnection();
        String sql = "SELECT * FROM boardtable3 ORDER BY bno DESC";

        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                Board board = new Board();
                board.setBno(rs.getInt(1));
                board.setBtitle(rs.getString(2));
                board.setBcontent(rs.getString(3));
                board.setBwriter(rs.getString(4));
                board.setBdate(rs.getDate(5));

                boardList.add(board);
            }
            return boardList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
       return null;
    }

    /** 글 한건 조회 */
    public Board selectOne(int bno){
        conn = DBUtil.getConnection();
        String sql = "SELECT * FROM boardtable3 WHERE bno = ?";

        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bno);
            try(ResultSet rs = pstmt.executeQuery()) {
                if(rs.next()) {
                    Board board = new Board();
                    board.setBno(rs.getInt(1));
                    board.setBtitle(rs.getString(2));
                    board.setBcontent(rs.getString(3));
                    board.setBwriter(rs.getString(4));
                    board.setBdate(rs.getDate(5));

                    return board;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

// 글 수정 (제목, 내용만 수정)
   public boolean updateBoard(Board board) {
        String sql ="UPDATE boardtable3 set btitle = ?,bcontent =? Where bno =?";
        conn = DBUtil.getConnection();

        try(PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1,board.getBtitle());
            pstmt.setString(2,board.getBcontent());
            pstmt.setInt(3,board.getBno());
            int result = pstmt.executeUpdate();
            if (result > 0 ) return true;
                   }catch (SQLException e) {
            e.printStackTrace();
        }
     return false;
   }

   // 글 삭제

    public boolean deleteBoard(int bno) {
        conn = DBUtil.getConnection();
        String sql = "DELETE FROM boardtable3 WHERE bno = ?";
        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1,bno);
            int ack = ps.executeUpdate();
            if(ack > 0) return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
