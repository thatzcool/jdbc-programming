package Four.model;

import Four.vo.Board;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
//4조  Table명 : boardTable4
public class BoardDAO {
    private Connection conn;
    List<Board> boardList = new ArrayList<>();

    //글 등록기능
    public boolean createBoard(Board board) {
        conn = DBUtil.getConnection();
        String sql = "insert into boardTable4(btitle, bcontent, bwriter, bdate) values(?,?,?, now())";
        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, board.getBtitle());
            pstmt.setString(2, board.getBcontent());
            pstmt.setString(3, board.getBwriter());

            int result = pstmt.executeUpdate();

            if(result > 0) {return true;}
        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
   }

    /*** 전체 글 조회 (최신순으로 정렬)*/
    public List<Board> selectAll(){
        conn = DBUtil.getConnection();
        String sql = "select * from boardTable4 order by bno desc";
        try(PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()){
            while(rs.next()) {
                Board temp = new Board();

                temp.setBno(rs.getInt("bno"));
                temp.setBtitle(rs.getString("btitle"));
                temp.setBcontent(rs.getString("bcontent"));
                temp.setBwriter(rs.getString("bwriter"));
                temp.setBdate(rs.getDate("bdate"));

                boardList.add(temp);
            }
            return boardList;
        }catch(SQLException e) {
            e.printStackTrace();
        }
       return null;
    }

    /** 글 한건 조회 */
    public Board selectOne(int bno){
        conn =DBUtil.getConnection();
        String sql = "select * from boardTable4 where bno = ?";
        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, bno);
            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    Board board = new Board();

                    board.setBno(rs.getInt("bno"));
                    board.setBtitle(rs.getString("btitle"));
                    board.setBcontent(rs.getString("bcontent"));
                    board.setBwriter(rs.getString("bwriter"));
                    board.setBdate(rs.getDate("bdate"));
                    return board;
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
// 글 수정 (제목, 내용만 수정)
   public boolean updateBoard(Board board) {
        conn = DBUtil.getConnection();
        String sql = "update boardtable4 set btitle = ?, bcontent = ? where bno = ?";
       try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
           pstmt.setString(1, board.getBtitle());
           pstmt.setString(2, board.getBcontent());
           pstmt.setInt(3, board.getBno());

           int result = pstmt.executeUpdate();

           if(result > 0) {
               for (Board b : boardList) {
                   if (b.getBno() == board.getBno()) {
                       b.setBtitle(board.getBtitle());
                       b.setBcontent(board.getBcontent());
                       return true;
                   }
               }
           }

       } catch (SQLException e) {
           e.printStackTrace();
       }
       return false;
   }

   // 글 삭제

    public boolean deleteBoard(int bno) {
        conn = DBUtil.getConnection();
        String sql = "delete from boardTable4 where bno = ?";

        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bno);
            int result = pstmt.executeUpdate();

            if(result > 0) {
                for (Board b : boardList) {
                    if (b.getBno() == bno) {
                        boardList.remove(b);
                        return true;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

}
