/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import dto.dtoctphieunhap;
import dto.dtonhacungcap;
import dto.dtophanloai;
import dto.dtophieunhap;
import dto.dtosanpham;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
/**
 *
 * @author giavi
 */
public class daosanpham {
    
    private ArrayList<dtosanpham> list_sp = new ArrayList();
    private ArrayList<dtonhacungcap> list_NCC = new ArrayList();
    private ArrayList<dtophanloai> listpl = new ArrayList<>();
    private ArrayList<dtophieunhap> list_Pn = new ArrayList<>();
    private ArrayList<dtoctphieunhap> list_Ctpn = new ArrayList<>();
    
    
    public ArrayList<dtosanpham> list() throws SQLException{
        Connection con = connect.connection();
        String sql = "SELECT * FROM sanpham where isHidden = 0";
        PreparedStatement pst =  con.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        while(rs.next()){
            dtosanpham sp = new dtosanpham();
            sp.setMaSanPham(rs.getInt("maSanPham"));
            sp.setTenSanPham(rs.getString("tenSanPham"));
            sp.setSoLuong(rs.getInt("soLuong"));
            sp.setNgayThem(rs.getDate("ngayThem"));
            sp.setMaPhanLoai(rs.getInt("maPhanLoai"));
            sp.setImg(rs.getString("img"));
            sp.setMaNCC(rs.getInt("maNhaCungCap"));
            sp.setHanSD(rs.getString("hanSuDung"));
            list_sp.add(sp);
        }
        con.close();
        return list_sp;
    }
    
    
    public ArrayList<dtosanpham> getList(){
        return list_sp;
    }
    
    
    public boolean addSanpham(dtosanpham sp) {
        String sql = "INSERT INTO sanpham (maPhanLoai, maSanPham, tenSanPham, soLuong, ngayThem, img, ishidden, maNhaCungCap, hanSuDung) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = connect.connection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, sp.getMaPhanLoai());
            pst.setInt(2, sp.getMaSanPham());
            pst.setString(3, sp.getTenSanPham());
//            pst.setDouble(4, sp.getGiaBan());
            pst.setInt(4, sp.getSoLuong());
            pst.setNull(5, java.sql.Types.TIMESTAMP);
            pst.setString(6, sp.getImg());
            pst.setInt(7, sp.getIshidden());
            pst.setInt(8, sp.getMaNCC());
            pst.setString(9, sp.getHanSD());

            int rowsInserted = pst.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void updateSanPham(dtosanpham sp) throws SQLException {
        String sql = "UPDATE sanpham SET tenSanPham = ?, maPhanLoai = ?, maNhaCungCap = ?, img = ? WHERE maSanPham = ?";
        Connection con = null;
        PreparedStatement pst = null;

        try {
            con = connect.connection();
            pst = con.prepareStatement(sql);
            pst.setString(1, sp.getTenSanPham());
            pst.setInt(2, sp.getMaPhanLoai());
            pst.setInt(3, sp.getMaNCC());
            pst.setString(4, sp.getImg());
            pst.setInt(5, sp.getMaSanPham());

            int rowsAffected = pst.executeUpdate(); // Thực hiện câu lệnh cập nhật
            if (rowsAffected > 0) {
                System.out.println("Cập nhật thành công!");
            } else {
                System.out.println("Không tìm thấy sản phẩm với mã đã cho.");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật sản phẩm: " + e.getMessage());
            throw e; // Ném lại ngoại lệ để xử lý ở nơi gọi phương thức
        } finally {
            // Đóng tài nguyên
            if (pst != null) {
                pst.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }
    
    public void deleteSanPham(Integer masp){
        String sql = "update sanpham set isHidden = 1 where maSanPham = ?";
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = connect.connection();
            pst = con.prepareStatement(sql);
            pst.setInt(1, masp);

            int rowsAffected = pst.executeUpdate(); // Thực hiện câu lệnh cập nhật
            if (rowsAffected > 0) {
                System.out.println("Cập nhật thành công!");
            } else {
                System.out.println("Không tìm thấy sản phẩm với mã đã cho.");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật sản phẩm: " + e.getMessage());
        }
    }


    
    
    
    public Integer getCountSanPham() {
        Integer count = 0;
        String sql = "SELECT COUNT(*) FROM sanpham WHERE isHidden = 0"; // Đếm các sản phẩm không bị ẩn

        try (Connection con = connect.connection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                count = rs.getInt(1); // Lấy giá trị của cột đầu tiên trong kết quả truy vấn (số lượng sản phẩm)
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    
    public Integer getMaPhanLoai(String tenPhanLoai) {
        String sql = "SELECT maPhanLoai FROM phanloai WHERE tenPhanLoai = ?";
        try (Connection con = connect.connection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, tenPhanLoai);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    // Trả về maPhanLoai nếu tìm thấy
                    return rs.getInt("maPhanLoai");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    public ArrayList<dtophanloai> listPhanloai() throws SQLException{
        String sql = "select * from phanloai where 1";
        Connection con = connect.connection();
        PreparedStatement pst = con.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        while(rs.next()){
            dtophanloai pl = new dtophanloai();
            pl.setMaPhanLoai(rs.getInt("maPhanLoai"));
            pl.setTenPhanLoai(rs.getString("tenPhanLoai"));
            listpl.add(pl);
        }
        return listpl;
    }
    public ArrayList<dtonhacungcap> listNCC() throws SQLException{
        String sql = "select * from nhacungcap where 1";
        Connection con = connect.connection();
        PreparedStatement pst = con.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        while(rs.next()){
            dtonhacungcap ncc = new dtonhacungcap();
            ncc.setMaNhaCungCap(rs.getInt("maNhaCungCap"));
            ncc.setTenNhaCungCap(rs.getString("tenNhaCungCap"));
            list_NCC.add(ncc);
        }
        return list_NCC;
    }

    public ArrayList<dtophieunhap> listPN(Integer maNCC) throws SQLException{
        String sql = "select * from phieunhap where maNhaCungCap = ?";
        Connection con = connect.connection();
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, maNCC);
        ResultSet rs = pst.executeQuery();
        while(rs.next()){
            dtophieunhap pn = new dtophieunhap();
            pn.setMaPhieuNhap(rs.getInt("maPhieuNhap"));
            pn.setNgayNhap(rs.getDate("ngayNhap"));
            list_Pn.add(pn);
        }
        return list_Pn;
    }
    
    
    public ArrayList<dtoctphieunhap> listCTPN(Integer mapn) throws SQLException {
        String sql = "SELECT * FROM chitietphieunhap WHERE maPhieuNhap = ? AND ishidden = 0";
        Connection con = connect.connection();
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, mapn);
        ResultSet rs = pst.executeQuery();

        ArrayList<dtoctphieunhap> list_Ctpn = new ArrayList<>();

        while (rs.next()) {
            dtoctphieunhap ctpn = new dtoctphieunhap();
            ctpn.setMaSanPham(rs.getInt("maSanPham"));
            ctpn.setSoluongtonkho(rs.getInt("soLuongTonKho"));
            ctpn.setGiaBan(rs.getDouble("giaBan"));
            ctpn.setNgayhethan(rs.getDate("ngayhethan"));
            list_Ctpn.add(ctpn);
        }

        return list_Ctpn;
    }

    
    
    
    
    
    public static void main(String[] args) throws SQLException {
        daosanpham dao = new daosanpham();
//        ArrayList<dtophieunhap> l = new ArrayList<>();
//        l = dao.listPN(2);
//        for(dtophieunhap pn : l){
//            System.out.println(pn.getMaPhieuNhap());
//        }
        ArrayList<dtoctphieunhap> l1 = new ArrayList<>();
        l1 = dao.listCTPN(21);
        for(dtoctphieunhap pn1 : l1){
            System.out.println(pn1.getMaSanPham());
        }
    }
    
}
