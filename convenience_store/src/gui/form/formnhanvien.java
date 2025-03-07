package gui.form;

import bus.busnhanvien;
import bus.bustaikhoan;
import gui.comp.NVCard;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.toedter.calendar.JDateChooser;
import net.miginfocom.swing.MigLayout;
import gui.layout.ResponsiveLayout;
import gui.model.ModelEmployee;
import dto.SampleData;
import dto.dtochucvu;
import dto.dtohopdong;
import dto.dtonhanvien;
import dto.dtotaikhoan;
import gui.comp.RoundedBorder;
import gui.simple.SimpleInputForms;
import gui.simple.SimpleInputFormsNhanVien;
import gui.swing.dashboard.Form;
import gui.swing.dashboard.SystemForm;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import raven.modal.ModalDialog;
import raven.modal.component.SimpleModalBorder;
import raven.modal.option.Location;
import raven.modal.option.Option;


@SystemForm(name = "Responsive Layout", description = "responsive layout user interface", tags = {"card"})
public class formnhanvien extends Form {
    
    
    private busnhanvien busNV;
    private bustaikhoan busTK;
    private ArrayList<dtonhanvien> list_NV;
    private ArrayList<dtochucvu> list_CV;
    private JLabel imageDisplayLabel;
    private File selectedFile;
    private JComboBox<Object> comboxNCC;
    private JComboBox<Object> comboxCV;
    private JTextField[] textField = new JTextField[20];
    private JComboBox<String> genderComboBox;
    private JDateChooser dateChooser;
    private ArrayList<dtohopdong> list_HD;
    
    public formnhanvien() throws SQLException {
        init();
        formInit();
        
    }

    private void init() throws SQLException {
        cards = new ArrayList<>();
        setLayout(new MigLayout("wrap,fill,insets 7 15 7 15", "[fill]", "[grow 0][fill]"));
        add(createHeaderAction());
        add(createExample());
       
    }

    
    // Đây là chỗ để tạo các card nhân viên
    @Override
    public void formInit() {
        panelCard.removeAll();
        try {
            busNV = new busnhanvien();
            busNV.list();
            list_NV = busNV.getList();
            list_HD = busNV.list_HD();
          
            for(int i = 0 ; i < list_NV.size() ; i++){
                dtonhanvien nv = list_NV.get(i);
                
                boolean isExist = false;
                for(dtohopdong hd : list_HD){
                    if(hd.getMaNV() == nv.getManhanvien()){
                        nv.setLuongcoban((float) hd.getLuongCoBan());
                        NVCard card1 = new NVCard(nv, createEventCard1() , 1);
                        cards.add(card1);
                        panelCard.add(card1);
                        isExist = true;
                        break;
                    }
                }
                
                if(!isExist){
                    NVCard card1 = new NVCard(nv, createEventCard1() , 1);
                    cards.add(card1);
                    panelCard.add(card1);
                }
                
                
            }
            
            panelCard.repaint();
            panelCard.revalidate();
        } catch (SQLException ex) {
        }
    }
    
    private Consumer<dtonhanvien> createEventCard1() {
        return e -> {
            try {
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                int dialogWidth = (int) (screenSize.width * 0.7);
                int dialogHeight = (int) (screenSize.height * 0.7);

                JDialog showDialog = new JDialog();
                showDialog.setSize(dialogWidth, dialogHeight);
                showDialog.setUndecorated(true); // Bỏ khung mặc định của JDialog
                showDialog.setShape(new RoundRectangle2D.Double(0, 0, dialogWidth, dialogHeight, 30, 30)); // Bo góc
                showDialog.setLayout(new BorderLayout());
                showDialog.setModal(true);

                JPanel titleBar = new JPanel();
                titleBar.setLayout(new BorderLayout());
                titleBar.setBackground(Color.LIGHT_GRAY);

                JLabel titleLabel = new JLabel("Thông tin nhân viên");
                titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
                titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
                titleLabel.setPreferredSize(new Dimension(dialogWidth, 50));

                JButton closeButton = new JButton("X");
                closeButton.setFocusPainted(false);
                closeButton.setBorderPainted(false);
                closeButton.setBackground(Color.RED);
                closeButton.setForeground(Color.WHITE);
                closeButton.setPreferredSize(new Dimension(45, 30));

                // Sự kiện đóng cửa sổ khi nhấn nút X
                closeButton.addActionListener(event -> showDialog.dispose());

                titleBar.add(titleLabel, BorderLayout.WEST);
                titleBar.add(closeButton, BorderLayout.EAST);

                // Tạo JPanel chứa thông tin nhân viên và ảnh
                JPanel contentPanel = new JPanel(new GridBagLayout());
                contentPanel.setBackground(Color.WHITE);

                // Panel bên trái chứa thông tin nhân viên

                JPanel nv_panel = new JPanel(new GridBagLayout());
                nv_panel.setBackground(Color.WHITE); // Đặt nền trắng cho panel
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5, 5, 5, 5); // Đặt khoảng cách giữa các thành phần
                gbc.fill = GridBagConstraints.HORIZONTAL;

                //Tạo panel bên trái
                addField(nv_panel, gbc, "Mã nhân viên:", 0, String.valueOf(e.getManhanvien()));
                addField(nv_panel, gbc, "Tên nhân viên:", 1, String.valueOf(e.getTennhanvien()));
                addField(nv_panel, gbc, "Giới tính:", 2, String.valueOf(e.getGioitinh())); 
                addField(nv_panel, gbc, "Năm sinh:", 3, String.valueOf(e.getNgaysinh()));  
                addField(nv_panel, gbc, "Số điện thoại:", 4, String.valueOf(e.getSdt()));
                addField(nv_panel, gbc, "Địa chỉ:", 5,  String.valueOf(e.getDiachi()));
                addField(nv_panel, gbc, "Email:", 6, String.valueOf(e.getEmail()));
                addField(nv_panel, gbc, "Lương:", 7, String.valueOf(e.getLuongcoban()));
                addField(nv_panel, gbc, "Tên chức vụ:", 8,busNV.getTenChucVu(e.getMachucvu()));


                // Tạo JPanel bên phải cho hình ảnh
                JPanel imagePanel = new JPanel(new GridBagLayout());
                imagePanel.setBackground(Color.WHITE);
                GridBagConstraints rightGbc = new GridBagConstraints();
                rightGbc.insets = new Insets(5, 5, 5, 5);
                addImageField(imagePanel,rightGbc, e.getImg(), showDialog);

                GridBagConstraints mainGbc = new GridBagConstraints();
                mainGbc.insets = new Insets(10, 10, 10, 10);
                mainGbc.fill = GridBagConstraints.BOTH;
                mainGbc.gridx = 0;
                mainGbc.gridy = 0;
                mainGbc.weightx = 0.6;
                contentPanel.add(nv_panel, mainGbc);

                mainGbc.gridx = 1;
                mainGbc.weightx = 0.4;
                contentPanel.add(imagePanel, mainGbc);



                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Đặt vị trí nút về phía phải
                buttonPanel.setBackground(Color.WHITE);

                JButton saveButton = new JButton("Lưu");
                saveButton.setPreferredSize(new Dimension(100, 30));
                saveButton.setBackground(Color.GREEN); // Đặt màu nền xanh lá
                saveButton.setForeground(Color.WHITE); // Đặt màu chữ trắng

                saveButton.addActionListener(event -> {
                    try {
                        dtonhanvien nv = new dtonhanvien();
                        nv.setManhanvien(Integer.parseInt(textField[0].getText()));
                        nv.setTennhanvien(textField[1].getText());
                        nv.setGioitinh(genderComboBox.getSelectedIndex());
                        nv.setNgaysinh(dateChooser.getDate());
                        nv.setSdt(textField[4].getText()); // lưu ý vị trí trường sdt
                        nv.setDiachi(textField[5].getText());
                        nv.setEmail(textField[6].getText());
                        nv.setMachucvu(busNV.getMaChucVuByName((String) comboxCV.getSelectedItem()));

                        

                        int result = JOptionPane.showConfirmDialog(null, "Bạn có chắc sửa thông tin nhân viên này không ?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                        if(result == JOptionPane.YES_OPTION){
                            if (selectedFile != null) {
                                nv.setImg(selectedFile.getName()); 
                                String root_dir = System.getProperty("user.dir") +  "/src/source/image/nhanvien/" ;
                                saveImageToDirectory(root_dir);
                                
                            }else{
                                nv.setImg(e.getImg());
                            }
                            
                            busNV.updateNhanVien(nv);
                            formInit();
                            
                            JOptionPane.showMessageDialog(null, "Cập nhật thành công");
                            showDialog.dispose();
                        }


                    } catch (SQLException ex) {
                        Logger.getLogger(formnhanvien.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(null, "Có lỗi xảy ra trong quá trình cập nhật");
                    }
                });


                buttonPanel.add(saveButton);
                showDialog.add(titleBar, BorderLayout.NORTH);
                showDialog.add(contentPanel, BorderLayout.CENTER);
                showDialog.add(buttonPanel, BorderLayout.SOUTH); // Thêm nút lưu vào phía dưới
                showDialog.setLocationRelativeTo(null); // Đặt JDialog ở giữa màn hình
                showDialog.setVisible(true);
            } catch (SQLException ex) {
                Logger.getLogger(formnhanvien.class.getName()).log(Level.SEVERE, null, ex);
            }
        };
    }

    
    private void addImageField(JPanel panel, GridBagConstraints gbc, String imgPath, JDialog showDialog) {
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel imgLabel = new JLabel("IMG:");
        imgLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        imgLabel.setPreferredSize(new Dimension(150, 20));
        panel.add(imgLabel, gbc);

        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        imageDisplayLabel = new JLabel();
        imageDisplayLabel.setPreferredSize(new Dimension(170, 210));
        if(!imgPath.isEmpty()){
            ImageIcon curImg = new ImageIcon(System.getProperty("user.dir") + "/src/source/image/nhanvien/" + imgPath);
            Image scaledImg = curImg.getImage().getScaledInstance(170, 230, Image.SCALE_SMOOTH);
            ImageIcon editImg = new ImageIcon(scaledImg);
            imageDisplayLabel.setIcon(editImg);

        }
        panel.add(imageDisplayLabel, gbc);

        gbc.gridy = 2;
        JButton fileChooserButton = new JButton("Chọn ảnh");
        fileChooserButton.setPreferredSize(new Dimension(100, 30));
        fileChooserButton.addActionListener(event -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setDialogTitle("Chọn ảnh");

            int result = fileChooser.showOpenDialog(showDialog);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                updateImageLabel(imageDisplayLabel, selectedFile.getPath());
            }
        });
        panel.add(fileChooserButton, gbc);
    }

    private void updateImageLabel(JLabel label, String imgPath) {
        if (imgPath != null && !imgPath.isEmpty()) {
            ImageIcon imageIcon = new ImageIcon(new ImageIcon(imgPath).getImage().getScaledInstance(170, 
                    230, Image.SCALE_SMOOTH));
            label.setIcon(imageIcon);
        } else {
            label.setIcon(null);
        }
    }
    
    
    private void saveImageToDirectory(String destinationDir) {
        try {
            File destinationDirFile = new File(destinationDir);

            if (!destinationDirFile.exists()) {
                destinationDirFile.mkdirs(); 
            }

            Path destinationPath = Paths.get(destinationDir, selectedFile.getName());

            Files.copy(selectedFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Image saved to: " + destinationPath.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    
    private void addField(JPanel panel, GridBagConstraints gbc, String label, int row, String value) throws SQLException {
        // Thêm label với kích thước lớn hơn
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0; // Không chiếm thêm không gian ngang cho label
        gbc.fill = GridBagConstraints.NONE; // Label sẽ có kích thước tự nhiên
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Arial", Font.PLAIN, 16)); // Thiết lập kích thước phông chữ lớn hơn
        jLabel.setPreferredSize(new Dimension(150, 30)); // Thiết lập kích thước cố định cho label (rộng và cao)
        panel.add(jLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0; // Chiếm toàn bộ không gian ngang còn lại
        gbc.fill = GridBagConstraints.HORIZONTAL; // TextField sẽ lấp đầy không gian ngang

        if (label.equals("Giới tính:")) {
            // Nếu là trường giới tính, tạo JComboBox
            genderComboBox = new JComboBox<>(new String[]{"Nữ", "Nam"});
            genderComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
            genderComboBox.setPreferredSize(new Dimension(150, 40)); 
            if(value.equals("1")){
                genderComboBox.setSelectedIndex(1); // Thiết lập giá trị mặc định
            }else{
                genderComboBox.setSelectedIndex(0);
            }
            panel.add(genderComboBox, gbc);
        } else if (label.equals("Năm sinh:")) {
            dateChooser = new JDateChooser();
            dateChooser.setFont(new Font("Arial", Font.PLAIN, 16));
            dateChooser.setPreferredSize(new Dimension(150, 40)); 

            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(value); // Đặt giá trị mặc định từ string
                dateChooser.setDate(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            panel.add(dateChooser, gbc);
        }else if(label.equals("Tên chức vụ:")){
            comboxCV = new JComboBox<>();
            comboxCV.setFont(new Font("Arial", Font.PLAIN, 16));
            comboxCV.setPreferredSize(new Dimension(150, 40)); 
            comboxCV.setBorder(new RoundedBorder(10));

            list_CV = busNV.listChucVu();
            for(dtochucvu cv : list_CV){
                comboxCV.addItem(cv.getTenchucvu());
            }
            
            comboxCV.setSelectedItem(value);
            panel.add(comboxCV, gbc);
        } else {
            // Nếu không phải, tạo JTextField bình thường
            textField[row] = new JTextField(20);
            textField[row].setFont(new Font("Arial", Font.PLAIN, 16));
            textField[row].setPreferredSize(new Dimension(150, 40)); 
            textField[row].setMinimumSize(new Dimension(150, 40)); 
            textField[row].setText(value);
            textField[row].setBorder(new RoundedBorder(10));
            if(row == 0 || row == 7){
                textField[row].setEditable(false);
            }
            panel.add(textField[row], gbc);
        }
    }



     private Component createHeaderAction() throws SQLException {
        JPanel panel = new JPanel(new MigLayout("insets 5 20 5 20", "[fill,230][fill,100][fill,100][fill,100]push[][]"));

        JTextField txtSearch = new JTextField();
        txtSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Tìm kiếm tên nhân viên...");
        txtSearch.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, resizeIcon(new ImageIcon(getClass().getResource("/source/image/icon/search.png")), 20, 20));

        JButton btnSearch = new JButton("Tìm kiếm");
        
        JComboBox comboMacv = new JComboBox();
        comboMacv.addItem("Mặc định");
        busNV = new busnhanvien();
        list_CV =  busNV.listChucVu();
        for(dtochucvu cv : list_CV){
            comboMacv.addItem(cv.getTenchucvu());
        }
        
        JButton btnReset = new JButton("Reset");

        JCheckBox selectAllCheckbox = new JCheckBox("Chọn tất cả");
        selectAllCheckbox.addActionListener(e -> selectAll(selectAllCheckbox.isSelected()));

        JButton cmdCreate = new JButton("Thêm");
        JButton cmdDelete = new JButton("Khóa");
        
        
        btnSearch.addActionListener(e -> {
            panelCard.removeAll(); // Xóa các card cũ khỏi panelCard
            String searchText = txtSearch.getText().toLowerCase().trim(); // Lấy chuỗi tìm kiếm và loại bỏ khoảng trắng thừa
            String tencv = (String) comboMacv.getSelectedItem();
            try {
                busNV.list();
                list_NV = busNV.getList();
            } catch (SQLException ex) {
                Logger.getLogger(formnhanvien.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(!tencv.equals("Mặc định")){
                ArrayList<dtonhanvien> list_nv_tmp = new ArrayList<>();
                for(dtonhanvien nv : list_NV){
                        try {
                            if(nv.getMachucvu() == busNV.getMaChucVuByName(tencv)){
                                list_nv_tmp.add(nv);
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(formnhanvien.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                if(searchText.equals("")){
                    for (dtonhanvien nv : list_nv_tmp) {
                        NVCard card = new NVCard(nv, createEventCard1(), 1);
                        cards.add(card);
                        panelCard.add(card);
                    }
                    panelCard.repaint();
                    panelCard.revalidate();
                }else{
                    for (dtonhanvien nv : list_nv_tmp) {
                        String tenNhanVien = nv.getTennhanvien().toLowerCase();
                        if (tenNhanVien.contains(searchText)) {
                            NVCard card = new NVCard(nv, createEventCard1(), 1);
                            cards.add(card);
                            panelCard.add(card);
                        }
                    }
                    panelCard.repaint();
                    panelCard.revalidate();
                }
                
                
                
                
            }else{
                if (searchText.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Vui lòng nhập từ khóa để tìm kiếm.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                boolean found = false; 

                for (dtonhanvien nv : list_NV) {
                    String tenNhanVien = nv.getTennhanvien().toLowerCase();
                    if (tenNhanVien.contains(searchText)) {
                        NVCard card = new NVCard(nv, createEventCard1(), 1);
                        cards.add(card);
                        panelCard.add(card);
                        found = true;
                    }
                }
                if (!found) {
                    JOptionPane.showMessageDialog(null, "Không tìm thấy nhân viên tương ứng.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
                panelCard.repaint();
                panelCard.revalidate();
            }
        });


        
        btnReset.addActionListener( e -> {
            txtSearch.setText("");
            comboMacv.setSelectedIndex(0);
            formInit();
        });
        
        cmdDelete.addActionListener(e -> {
            try {
                if (selectAllCheckbox.isSelected()) {
                    selectAllCheckbox.setSelected(false);
                }
                deleteSelectedCards();
            } catch (SQLException ex) {
                Logger.getLogger(formnhanvien.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        cmdCreate.addActionListener(e -> {
            try {
                showModal();
            } catch (SQLException ex) {
                Logger.getLogger(formnhanvien.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        panel.add(txtSearch, "span 1 1"); // Mở rộng không gian của trường tìm kiếm
        panel.add(comboMacv);
        panel.add(btnSearch);
        panel.add(btnReset);
        panel.add(selectAllCheckbox);
        panel.add(cmdCreate);
        panel.add(cmdDelete);

        panel.putClientProperty(FlatClientProperties.STYLE, "background:null;");
        return panel;
    }

     private void selectAll(boolean selected) {
        for (NVCard card : cards) {
            card.setSelected(selected);
        }
    }
        private void deleteSelectedCards() throws SQLException {         
            List<NVCard> selectedCards = new ArrayList<>();
            StringBuilder deletedNames = new StringBuilder("Đã xóa các nhân viên: ");

            // Lọc danh sách nhân viên được chọn
            for (NVCard card : cards) {
                if (card.isSelected()) { 
                    selectedCards.add(card);
                }
            }

            // Kiểm tra nếu không có nhân viên nào được chọn
            if (selectedCards.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không có nhân viên nào được chọn để xóa.");
                return;
            }

            // Xác nhận trước khi xóa
            int result = JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa các nhân viên vừa chọn không?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.NO_OPTION) {
                return;
            }

            busNV = new busnhanvien();
            busTK = new bustaikhoan();

            for (NVCard card : selectedCards) {
                // Lấy mã nhân viên từ card
                int manv = card.getMaNhanVien();

                // Xóa nhân viên
                busNV.deleteNhanVien(manv);  // Không cần kiểm tra thành công nếu phương thức không trả về giá trị

                // Xóa tài khoản của nhân viên
                boolean deleteAccountSuccess = busTK.deleteTaiKhoan(String.valueOf(manv));

                // Nếu xóa tài khoản thành công, xóa thẻ nhân viên khỏi giao diện
                if (deleteAccountSuccess) {
                    deletedNames.append(card.getEmployeeName()).append(", ");
                    panelCard.remove(card);  // Xóa card khỏi giao diện
                    cards.remove(card);      // Xóa card khỏi danh sách
                } else {
                    // Nếu không thể xóa tài khoản, thông báo lỗi cho từng nhân viên
                    JOptionPane.showMessageDialog(this, "Không thể xóa tài khoản cho nhân viên: " + card.getEmployeeName() + ". Vui lòng kiểm tra lại.");
                }
            }

            // Cập nhật lại giao diện
            panelCard.revalidate();
            panelCard.repaint();

            // Hiển thị kết quả xóa
            if (deletedNames.length() > 0) {
                deletedNames.setLength(deletedNames.length() - 2);  // Xóa dấu phẩy và khoảng trắng cuối cùng
                JOptionPane.showMessageDialog(this, "Đã xóa " + selectedCards.size() + " nhân viên.\n" + deletedNames.toString());
            } else {
                JOptionPane.showMessageDialog(this, "Không có nhân viên nào được xóa.");
            }
        }

    public boolean check_NV(dtonhanvien nv) throws SQLException{
        String regexTenNV = "^[A-Za-zÀ-ỹ]+( [A-Za-zÀ-ỹ]+)*$";
        String regexSDT = "^0\\d{9}$";
        String regexEmail = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

        busNV = new busnhanvien();
        
        if (nv.getTennhanvien()== null || nv.getTennhanvien().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tên nhân viên không được để trống");
            return false;
        }
        if(!nv.getTennhanvien().matches(regexTenNV)){
            JOptionPane.showMessageDialog(null, "Tên nhân viên không chứa ký tự đặc biệt");
            return false;
        }
        
        if (nv.getSdt() == null || nv.getSdt().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Số điện thoại không được để trống");
            return false;
        }
        
        if(busNV.checkExistSdt(nv.getSdt())) {
            JOptionPane.showMessageDialog(null, "Số điện thoại đã tồn tại");
            return false;
        }
        
        if(!nv.getSdt().matches(regexSDT)){
            JOptionPane.showMessageDialog(null, "Số điện thoại phải bắt đầu bằng số 0 và đủ 10 số");
            return false;
        }
        
        if (nv.getEmail() == null || !nv.getEmail().matches(regexEmail)) {
            JOptionPane.showMessageDialog(null, "Email không hợp lệ");
            return false;
        }
        
        if(busNV.checkExistEmail(nv.getEmail())) {
            JOptionPane.showMessageDialog(null, "Email này đã tồn tại");
            return false;
        }
        
        

        
        
        Date ngaysinh = nv.getNgaysinh();
        if (ngaysinh != null) {
            Calendar calNgaySinh = Calendar.getInstance();
            calNgaySinh.setTime(ngaysinh);

            int namSinh = calNgaySinh.get(Calendar.YEAR);
            int namHienTai = Calendar.getInstance().get(Calendar.YEAR);
            int tuoi = namHienTai - namSinh;

            // Kiểm tra tuổi, nếu nhỏ hơn 18 tuổi thì báo lỗi
            if (tuoi < 18 || (tuoi == 18 && calNgaySinh.after(Calendar.getInstance()))) {
                JOptionPane.showMessageDialog(null, "Nhân viên phải lớn hơn 18 tuổi");
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(null, "Không được để trống năm sinh nhân viên");
            return false;
        }

        if(nv.getImg().isEmpty()){
            JOptionPane.showMessageDialog(null, "Vui lòng chọn hình ảnh cần thêm");
            return false;
        }

        
        
        return true;
    }

    
    private void showModal() throws SQLException {
    Option option = ModalDialog.createOption();
    option.getLayoutOption().setSize(-1, 1f)
            .setLocation(Location.TRAILING, Location.TOP)
            .setAnimateDistance(0.7f, 0);

    SimpleInputFormsNhanVien form = new SimpleInputFormsNhanVien();
    ModalDialog.showModal(this, new SimpleModalBorder(
            form, "Create", SimpleModalBorder.YES_NO_OPTION,
            (controller, action) -> {
                if (action == SimpleModalBorder.YES_OPTION) {
                    int result = JOptionPane.showConfirmDialog(null, "Bạn có chắc thêm nhân viên này không", "Xác nhận", JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.NO_OPTION) {
                        return; // Giữ nguyên modal nếu người dùng chọn NO
                    }

                    boolean isSuccessful = false; // Flag để kiểm tra thành công
                        busNV = new busnhanvien();

                    try {
                        dtonhanvien nv = form.getNhanVien();
                             if (check_NV(nv)) {
                        selectedFile = form.getSelectedFile();
                        String root_dir = System.getProperty("user.dir") + "/src/source/image/nhanvien/";

                        // Thêm nhân viên
                        busNV.AddNhanVien(nv);

                        // Lưu ảnh (nếu có)
                        if (selectedFile != null) {
                            saveImageToDirectory(root_dir);
                        }
                        
                        busTK = new bustaikhoan();
                        // Tạo tài khoản cho nhân viên
                        dtotaikhoan newTaiKhoan = new dtotaikhoan();
                        newTaiKhoan.setTendangnhap(nv.getEmail());
                        newTaiKhoan.setMatkhau(nv.getSdt());
                        newTaiKhoan.setNgaytao(new Date());
                        newTaiKhoan.setIsblock(0);
                        newTaiKhoan.setManhanvien(nv.getManhanvien());

                        // In thông tin tài khoản để kiểm tra
                        System.out.println("Thông tin tài khoản:");
                        System.out.println("Tên đăng nhập: " + newTaiKhoan.getTendangnhap());
                        System.out.println("Mật khẩu: " + newTaiKhoan.getMatkhau());
                        System.out.println("Ngày tạo: " + newTaiKhoan.getNgaytao());
                        System.out.println("IsBlock: " + newTaiKhoan.getIsblock());
                        System.out.println("Mã nhân viên: " + newTaiKhoan.getManhanvien());
                        // Thêm tài khoản
                               boolean results = busTK.addTaikhoan(newTaiKhoan); // Sử dụng lại biến result
                               System.out.println("Kết quả thêm tài khoản: " + results);
                               if (results) {
                                   JOptionPane.showMessageDialog(null, "Thêm nhân viên và tài khoản thành công!");
                               } else {
                                   JOptionPane.showMessageDialog(null, "Thêm nhân viên thành công nhưng tạo tài khoản thất bại!");
                               }

                               formInit();
                           } else {
                               controller.close();
                           }
                       } catch (ParseException ex) {
                           Logger.getLogger(formnhanvien.class.getName()).log(Level.SEVERE, "Lỗi định dạng dữ liệu", ex);
                           JOptionPane.showMessageDialog(null, "Lỗi định dạng dữ liệu, vui lòng kiểm tra lại thông tin nhập vào.");
                       } catch (SQLException ex) {
                           Logger.getLogger(formnhanvien.class.getName()).log(Level.SEVERE, "Lỗi cơ sở dữ liệu", ex);
                           JOptionPane.showMessageDialog(null, "Lỗi trong quá trình lưu vào cơ sở dữ liệu, vui lòng thử lại.");
                       }
                    if (isSuccessful) {
                        controller.close(); // Chỉ đóng modal nếu tất cả các bước thành công
                    }
                } else if (action == SimpleModalBorder.NO_OPTION) {
                    controller.close(); // Đóng modal nếu người dùng chọn NO_OPTION
                }
            }), option);
}



     
     
     
     
    private Component createExample() {
        responsiveLayout = new ResponsiveLayout(ResponsiveLayout.JustifyContent.FIT_CONTENT, new Dimension(-1, -1), 10, 10);
        panelCard = new JPanel(responsiveLayout);
        panelCard.putClientProperty(FlatClientProperties.STYLE, "" +
                "border:10,10,10,10;");
        JScrollPane scrollPane = new JScrollPane(panelCard);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(10);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        scrollPane.getHorizontalScrollBar().putClientProperty(FlatClientProperties.STYLE, "" +
                "trackArc:$ScrollBar.thumbArc;" +
                "thumbInsets:0,0,0,0;" +
                "width:7;");
        scrollPane.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE, "" +
                "trackArc:$ScrollBar.thumbArc;" +
                "thumbInsets:0,0,0,0;" +
                "width:7;");
        scrollPane.setBorder(null);
        JSplitPane splitPane = new JSplitPane();
        splitPane.setLeftComponent(scrollPane);
        splitPane.setRightComponent(Box.createGlue());
        splitPane.setResizeWeight(1);
         splitPane.setDividerSize(0);
        return splitPane;
    }
    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
    Image img = icon.getImage();
    Image newImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    return new ImageIcon(newImg);
    }
    private List<NVCard> cards;
    private JPanel panelCard;
    private ResponsiveLayout responsiveLayout;
    
       
}
