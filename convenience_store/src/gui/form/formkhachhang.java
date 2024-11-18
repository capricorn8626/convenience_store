package gui.form;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import dto.dtokhachhang;
import bus.buskhachhang;

public class formkhachhang extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private JTextField txtMaKH, txtSDT, txtTenKH, txtDiemTichLuy, txtMaUuDai;
    private JButton btnThem, btnSua, btnXoa, btnHuy;
    
    public formkhachhang() {
        initUI();
        loadDataToTable();
    }

    private void initUI() {
        // Table setup
        String[] columns = {"Mã KH", "SĐT", "Tên KH", "Điểm tích lũy", "Mã ưu đãi"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0 && column != 3;  // Disable editing for MaKH and DiemTichLuy
            }
        };
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        table.setGridColor(Color.GRAY);
        table.setShowGrid(true);
        
        JScrollPane scrollPane = new JScrollPane(table);

        // Panel for displaying data details
        JPanel panelDetails = new JPanel(new GridLayout(5, 2, 10, 10));
        panelDetails.setBorder(BorderFactory.createTitledBorder("Thông tin khách hàng"));
        panelDetails.add(new JLabel("Mã Khách Hàng:"));
        txtMaKH = new JTextField();
        txtMaKH.setEnabled(false);  // Không cho chỉnh sửa mã khách hàng
        panelDetails.add(txtMaKH);
        panelDetails.add(new JLabel("SĐT:"));
        txtSDT = new JTextField();
        panelDetails.add(txtSDT);
        panelDetails.add(new JLabel("Tên Khách Hàng:"));
        txtTenKH = new JTextField();
        panelDetails.add(txtTenKH);
        panelDetails.add(new JLabel("Điểm tích lũy:"));
        txtDiemTichLuy = new JTextField();
        txtDiemTichLuy.setEnabled(false);  // Không chỉnh sửa điểm tích lũy
        panelDetails.add(txtDiemTichLuy);
        panelDetails.add(new JLabel("Mã ưu đãi:"));
        txtMaUuDai = new JTextField();
        panelDetails.add(txtMaUuDai);

        // Panel for buttons
        JPanel panelButtons = createButtonPanel();

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(panelDetails, BorderLayout.SOUTH);
        add(panelButtons, BorderLayout.NORTH);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.setPreferredSize(new Dimension(100, 40));  // Adjust height of button panel

        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnHuy = new JButton("Reset");

        // Set button size
        btnThem.setPreferredSize(new Dimension(70, 30));
        btnSua.setPreferredSize(new Dimension(70, 30));
        btnXoa.setPreferredSize(new Dimension(70, 30));
        btnHuy.setPreferredSize(new Dimension(70, 30));

        // Add buttons to panel
        panel.add(btnThem);
        panel.add(btnSua);
        panel.add(btnXoa);
        panel.add(btnHuy);

        addEventHandlers();  // Attach event handlers
        return panel;
    }

    private void loadDataToTable() {
        model.setRowCount(0);
        buskhachhang busKH = new buskhachhang();
        ArrayList<dtokhachhang> listKH = busKH.getAllKhachHang();

        for (dtokhachhang kh : listKH) {
            model.addRow(new Object[] {
                kh.getMaKhachHang(),
                kh.getSDT(),
                kh.getTenKhachHang(),
                kh.getDiemTichLuy(),
                kh.getMaUudai()
            });
        }
    }

    private void addEventHandlers() {
        // Table row selection handler
        table.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                txtMaKH.setText(model.getValueAt(selectedRow, 0).toString());
                txtSDT.setText(model.getValueAt(selectedRow, 1).toString());
                txtTenKH.setText(model.getValueAt(selectedRow, 2).toString());
                txtDiemTichLuy.setText(model.getValueAt(selectedRow, 3).toString());
                txtMaUuDai.setText(model.getValueAt(selectedRow, 4).toString());
            }
        });

        // Button handlers
        btnThem.addActionListener(e -> onAddCustomer());
        btnSua.addActionListener(e -> onEditCustomer());
        btnXoa.addActionListener(e -> onDeleteCustomer());
        btnHuy.addActionListener(e -> clearForm());
    }

   private void onAddCustomer() {
    // Lấy thông tin từ các trường nhập liệu
    String sdt = txtSDT.getText();
    String tenKH = txtTenKH.getText();
    String diemTichLuyStr = "0";
//            txtDiemTichLuy.getText();
    String maUuDaiStr = txtMaUuDai.getText();

    // Kiểm tra nếu các trường dữ liệu trống
    if (sdt.isEmpty() || tenKH.isEmpty()|| maUuDaiStr.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
        return;
    }
// Kiểm tra số điện thoại phải là số và có đúng 10 chữ số
        if (!sdt.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại phải là 10 chữ số!");
            return;
        }

        // Kiểm tra tên khách hàng chỉ chứa chữ hoa, thường và khoảng trắng
        if (!tenKH.matches("[a-zA-Z\\s]+")) {
            JOptionPane.showMessageDialog(this, "Tên khách hàng chỉ chứa chữ cái và khoảng trắng!");
            return;
        }

    try {
        // Chuyển đổi điểm tích lũy và mã ưu đãi thành số nguyên
                buskhachhang busKH = new buskhachhang();

        int diemTichLuy = Integer.parseInt(diemTichLuyStr);
        int maUuDai = Integer.parseInt(maUuDaiStr);
        int maKH = busKH.getSoLuongKH() + 1;

        // Tạo đối tượng khách hàng mới
        dtokhachhang newKhachHang = new dtokhachhang(maKH ,sdt, tenKH, diemTichLuy, maUuDai);

        // Thêm khách hàng vào cơ sở dữ liệu
//        buskhachhang busKH = new buskhachhang();
        boolean result = busKH.addKhachHang(newKhachHang);

        if (result) {
            loadDataToTable();  // Tải lại dữ liệu vào bảng
            clearForm();  // Xóa dữ liệu trên form
            JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm khách hàng!");
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Điểm tích lũy và Mã ưu đãi phải là số!");
    }
}

private void onEditCustomer() {
    try {
        // Lấy thông tin từ các trường nhập liệu
        int maKH = Integer.parseInt(txtMaKH.getText());
        String sdt = txtSDT.getText();
        String tenKH = txtTenKH.getText();
        String diemTichLuyStr = txtDiemTichLuy.getText();
        String maUuDaiStr = txtMaUuDai.getText();

        // Kiểm tra nếu các trường dữ liệu trống
        if (sdt.isEmpty() || tenKH.isEmpty() || diemTichLuyStr.isEmpty() || maUuDaiStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }
// Kiểm tra số điện thoại phải là số và có đúng 10 chữ số
        if (!sdt.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại phải là 10 chữ số!");
            return;
        }

        // Kiểm tra tên khách hàng chỉ chứa chữ hoa, thường và khoảng trắng
        if (!tenKH.matches("[a-zA-Z\\s]+")) {
            JOptionPane.showMessageDialog(this, "Tên khách hàng chỉ chứa chữ cái và khoảng trắng!");
            return;
        }

        // Chuyển đổi điểm tích lũy và mã ưu đãi thành số nguyên
        int diemTichLuy = Integer.parseInt(diemTichLuyStr);
        int maUuDai = Integer.parseInt(maUuDaiStr);

        // Tạo đối tượng khách hàng mới với thông tin đã chỉnh sửa
        dtokhachhang customer = new dtokhachhang(maKH, sdt, tenKH, diemTichLuy, maUuDai);

        // Cập nhật thông tin khách hàng vào cơ sở dữ liệu
        buskhachhang busKH = new buskhachhang();
        boolean result = busKH.updateKhachHang(customer);

        if (result) {
            loadDataToTable();
            JOptionPane.showMessageDialog(this, "Lưu thông tin khách hàng thành công!");
        } else {
            JOptionPane.showMessageDialog(this, "Lưu thông tin khách hàng thất bại!");
        }
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng số cho các trường Mã KH, Điểm tích lũy, Mã ưu đãi!");
    }
}

    private void onDeleteCustomer() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int confirmation = JOptionPane.showConfirmDialog(this,
                    "Bạn chắc chắn muốn xóa khách hàng này?", 
                    "Xác nhận xóa", 
                    JOptionPane.YES_NO_OPTION);

            if (confirmation == JOptionPane.YES_OPTION) {
                int maKH = Integer.parseInt(txtMaKH.getText());

                buskhachhang busKH = new buskhachhang();
                boolean result = busKH.deleteKhachHang(maKH);

                if (result) {
                    model.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(this, "Xóa khách hàng thành công!");
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa khách hàng thất bại!");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần xóa!");
        }
    }

    private int generateNewCustomerCode() {
        buskhachhang busKH = new buskhachhang();
        int newCustomerCode = busKH.getNextCustomerCode();
        return newCustomerCode;
    }

    private void clearForm() {
        txtMaKH.setText("");
        txtSDT.setText("");
        txtTenKH.setText("");
        txtDiemTichLuy.setText("");
        txtMaUuDai.setText("");
        table.clearSelection();
//        enableEditing(false);
    }

    private void enableEditing(boolean enable) {
        txtSDT.setEnabled(enable);
        txtTenKH.setEnabled(enable);
        txtMaUuDai.setEnabled(enable);
    }
}
