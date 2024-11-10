/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author ADMIN
 */
public class dtoctphieunhap {
    public dtoctphieunhap(){}

    public dtoctphieunhap(int maCTPhieuNhap, int soLuong, double giaNhap, int maPhieuNhap, int maSanPham, Timestamp ngayhethan, int soluongtonkho, String ghichu, double giaBan) {
        this.maCTPhieuNhap = maCTPhieuNhap;
        this.soLuong = soLuong;
        this.giaNhap = giaNhap;
        this.giaBan = giaBan;
        this.maPhieuNhap = maPhieuNhap;
        this.maSanPham = maSanPham;
        this.ngayhethan = ngayhethan;
        this.soluongtonkho = soluongtonkho;
        this.ghichu = ghichu;
        setIshidden();
    }

    public int getMaCTPhieuNhap() {
        return maCTPhieuNhap;
    }

    public void setMaCTPhieuNhap(int maCTPhieuNhap) {
        this.maCTPhieuNhap = maCTPhieuNhap;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getMaPhieuNhap() {
        return maPhieuNhap;
    }

    public void setMaPhieuNhap(int maPhieuNhap) {
        this.maPhieuNhap = maPhieuNhap;
    }

    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }

    public int getSoluongtonkho() {
        return soluongtonkho;
    }

    public void setSoluongtonkho(int soluongtonkho) {
        this.soluongtonkho = soluongtonkho;
    }

    public int getIshidden() {
        return ishidden;
    }

    public void setIshidden() {
        if(this.soluongtonkho == 0 || this.ngayhethan.before(new Date())) this.ishidden = 1;
        else this.ishidden = 0;
    }

    public String getGhichu() {
        return ghichu;
    }

    public void setGhichu(String ghichu) {
        this.ghichu = ghichu;
    }
    
    public double getthanhtien(){
        return (double)this.soLuong*this.giaNhap;
    }

    public double getGiaNhap() {
        return giaNhap;
    }

    public void setGiaNhap(double giaNhap) {
        this.giaNhap = giaNhap;
    }

    public double getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(double giaBan) {
        this.giaBan = giaBan;
    }

    public Timestamp getNgayhethan() {
        return ngayhethan;
    }

    public void setNgayhethan(Timestamp ngayhethan) {
        this.ngayhethan = ngayhethan;
    }
    
    private int maCTPhieuNhap;
    private int soLuong;
    private double giaNhap;
    private double giaBan;
    private int maPhieuNhap;
    private int maSanPham;
    private Timestamp ngayhethan;
    private int soluongtonkho;
    private int ishidden;
    private String ghichu;

    public String getTenSP(int masp){
        return "Ten sp";
    }
    @Override
    public String toString() {
        return "dtoctphieunhap{" + "maCTPhieuNhap=" + maCTPhieuNhap + ", soLuong=" + soLuong + ", giaNhap=" + giaNhap + ", giaBan=" + giaBan + ", maPhieuNhap=" + maPhieuNhap + ", maSanPham=" + maSanPham + ", ngayhethan=" + ngayhethan + ", soluongtonkho=" + soluongtonkho + ", ishidden=" + ishidden + ", ghichu=" + ghichu + '}';
    }

    public Object[] toTableRow() {
        return new Object[]{maSanPham , getTenSP(maSanPham), soLuong, giaNhap, ngayhethan, giaBan, soluongtonkho};
    }
}
