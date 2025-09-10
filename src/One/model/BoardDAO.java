package One.model;

import Four.vo.Board;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//1조  Table명 : boardTable1
public class BoardDAO {
    private Connection conn;
    List<Board> boardList = new ArrayList<>();

    //글 등록기능
    public boolean createBoard(Board board) {
        conn = DBUtil.getConnection();
        String sql = "INSERT INTO boardtable1(btitle, bcontent, bwriter, bdate) VALUES(?,?,?,now())";

        try (PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, board.getBtitle());
            pstmt.setString(2, board.getBcontent());
            pstmt.setString(3, board.getBwriter());

            int result = pstmt.executeUpdate();
            if (result > 0) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /*** 전체 글 조회 (최신순으로 정렬)*/
    public List<Board> selectAll() {
        List<Board> boardlist = new ArrayList<>();
        conn = DBUtil.getConnection();
        String sql = "select * from boardtable1 order by bno desc";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    Board board = new Board();
                    board.setBno(rs.getInt("bno"));
                    board.setBtitle(rs.getString("btile"));
                    board.setBcontent(rs.getString("bcontent"));
                    board.setBwriter(rs.getString("bwriter"));
                    board.setBdate(rs.getTimestamp("bdate"));
                    boardList.add(board);
                }
            }
            return boardList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 글 한건 조회
     */
    public Board selectOne(int bno) {
        conn = DBUtil.getConnection();
        String sql = "select * from boardtable1 where bno = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bno);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs != null) {
                    while (rs.next()) {
                        Board board = new Board();
                        board.setBno(rs.getInt("bno"));
                        board.setBtitle(rs.getString("btile"));
                        board.setBcontent(rs.getString("bcontent"));
                        board.setBwriter(rs.getString("bwriter"));
                        board.setBdate(rs.getTimestamp("bdate"));

                        return board
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // 글 수정 (제목, 내용만 수정)
    public boolean updateBoard(Board board) {
        conn = DBUtil.getConnection();
        String sql = "UPDATE boardTable1 SET btitle = ?, bcontent = ? WHERE bno = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1,board.getBtitle());
            pstmt.setString(2,board.getBcontent());
            pstmt.setInt(3,board.getBno());

            int ok = pstmt.executeUpdate();
            if(ok>0) return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 글 삭제

    public boolean deleteBoard(int bno) {
        conn = DBUtil.getConnection();
        String sql = "DELETE FROM boardTable1 WHERE bno = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1,bno);

            int ok = pstmt.executeUpdate();
            if(ok>0) return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

}
