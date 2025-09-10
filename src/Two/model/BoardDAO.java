package Two.model;

import Four.vo.Board;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//2조  Table명 : boardTable2
public class BoardDAO {
    private Connection conn;
    List<Board> boardList = new ArrayList<>();

    //글 등록기능
    public boolean createBoard(Board board) {
        conn = DBUtil.getConnection();

        String sql = "INSERT INTO boardTable2(btitle, bcontent, bwriter, bdate) VALUES (?, ?, ?, now())";

        try(PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, board.getBtitle());
            preparedStatement.setString(2, board.getBcontent());
            preparedStatement.setString(3, board.getBwriter());

            int result = preparedStatement.executeUpdate();
            if (result > 0) {
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

        String sql = "SELECT * FROM boardTable2 ORDER BY bno DESC";

        try(PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet != null) {

                while (resultSet.next()) {
                    Board board = new Board();

                    board.setBno(resultSet.getInt("bno"));
                    board.setBtitle(resultSet.getString("btitle"));
                    board.setBcontent(resultSet.getString("bcontent"));
                    board.setBwriter(resultSet.getString("bwriter"));
                    board.setBdate(resultSet.getDate("bdate"));

                    boardList.add(board);
                }
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
        String sql = "select * from boardTable2 where bno = ?";

        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1,bno);
            try( ResultSet rs = pstmt.executeQuery()) {
                if(rs != null){
                    Board board = new Board();
                    board.setBno(rs.getInt("bno"));
                    board.setBtitle(rs.getString("btitle"));
                    board.setBcontent(rs.getString("bcontent"));
                    board.setBwriter(rs.getString("bwriter"));
                    board.setBdate(rs.getDate("bdate"));
                    return board;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
// 글 수정 (제목, 내용만 수정)
   public boolean updateBoard(Board board) {
        conn = DBUtil.getConnection();
        String sql = "update boardTable2 set btitle = ?, bcontent = ? where bno = ?";

        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, board.getBtitle());
            pstmt.setString(2, board.getBcontent());
            pstmt.setInt(3, board.getBno());

            int result = pstmt.executeUpdate();
            if (result > 0) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
     return false;
   }

   // 글 삭제

    public boolean deleteBoard(int bno) {
        String Sql = "Delete from boardtable2 where bno = ?";

        Connection connection = DBUtil.getConnection();
        try(PreparedStatement ps = connection.prepareStatement(Sql)) {
            ps.setInt(1, bno);

            int result = ps.executeUpdate();
            if(result == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

}
