/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgfinal;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


/**
 *
 * @author Administrator
 */
public class frm extends javax.swing.JFrame {

    /**
     * Creates new form frm
     */
    int xx, yy, x, y;
    int[][] matran;
    int[][] matrandanh;
    JButton[][] bt;
    boolean winner;
    static boolean flat = false;
    ObjectOutputStream oos;
    ServerSocket serversocket;
    Socket socket;
    OutputStream os;// ....
    InputStream is;// ......
    String temp = "", strNhan = "";
    ObjectInputStream ois;

    public frm() {
        initComponents();
        x = 25;
        y = 25;
        
        matran = new int[x][y];
        matrandanh = new int[x][y];
        this.getContentPane().setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        p.setBounds(10, 30, 400, 400);
        p.setLayout(new GridLayout(x, y));
        bt = new JButton[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                final int a = i, b = j;
                bt[a][b] = new JButton();
                bt[a][b].setBackground(Color.LIGHT_GRAY);
                bt[a][b].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        flat = true;// server da click
                        //thoigian.start();
                        //second = 0;
                        //minute = 0;
                        matrandanh[a][b] = 1;
                        bt[a][b].setEnabled(false);
                        //bt[a][b].setIcon(new ImageIcon(getClass().getResource("o.png")));
                        bt[a][b].setBackground(Color.RED);
                        try {
                            oos.writeObject("caro," + a + "," + b);
                            setEnableButton(false);
                        } catch (Exception ie) {
                            ie.printStackTrace();
                        }
                        // thoigian.stop();
                    }

                });
                p.add(bt[a][b]);
                p.setVisible(false);
                p.setVisible(true);
            }
        }
        try {
            serversocket = new ServerSocket(1234);
            System.out.println("Dang doi client...");
            socket = serversocket.accept();
            System.out.println("Client da ket noi!");
            os = socket.getOutputStream();
            is = socket.getInputStream();
            oos = new ObjectOutputStream(os);
            ois = new ObjectInputStream(is);
            while (true) {
                String stream = ois.readObject().toString();
                String[] data = stream.split(",");
                if (data[0].equals("chat")) {
                    temp += "Khách:" + data[1] + '\n';
                    txtContent.setText(temp);
                } else if (data[0].equals("caro")) {
                    //thoigian.start();
                    // second = 0;
                    //minute = 0;
                    caro(data[1], data[2]);
                    setEnableButton(true);

                    if (winner == false) {
                        setEnableButton(true);
                    }
                } else if (data[0].equals("newgame")) {
                    newgame();
                    //second = 0;
                    //minute = 0;
                } else if (data[0].equals("checkwin")) {
                    //thoigian.stop();

                }
            }
        } catch (Exception ie) {
            // ie.printStackTrace();
        } //finally {
        //      socket.close();
        //      serversocket.close();
        //}

    }

    public void newgame() {
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                bt[i][j].setBackground(Color.LIGHT_GRAY);
                matran[i][j] = 0;
                matrandanh[i][j] = 0;
            }
        }
        setEnableButton(true);
        //second = 0;
        //minute = 0;
        //thoigian.stop();
    }

    public void setVisiblePanel(JPanel pHienthi) {
        this.add(pHienthi);
        pHienthi.setVisible(true);
        pHienthi.updateUI();// ......

    }

    public void setEnableButton(boolean b) {
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if (matrandanh[i][j] == 0) {
                    bt[i][j].setEnabled(b);
                }
            }
        }
    }

    //thuat toan tinh thang thua
    public int checkHang() {
        int win = 0, hang = 0, n = 0, k = 0;
        boolean check = false;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if (check) {
                    if (matran[i][j] == 1) {
                        hang++;
                        if (hang > 4) {
                            win = 1;
                            break;
                        }
                        continue;
                    } else {
                        check = false;
                        hang = 0;
                    }
                }
                if (matran[i][j] == 1) {
                    check = true;
                    hang++;
                } else {
                    check = false;
                }
            }
            hang = 0;
        }
        return win;
    }

    public int checkCot() {
        int win = 0, cot = 0;
        boolean check = false;
        for (int j = 0; j < y; j++) {
            for (int i = 0; i < x; i++) {
                if (check) {
                    if (matran[i][j] == 1) {
                        cot++;
                        if (cot > 4) {
                            win = 1;
                            break;
                        }
                        continue;
                    } else {
                        check = false;
                        cot = 0;
                    }
                }
                if (matran[i][j] == 1) {
                    check = true;
                    cot++;
                } else {
                    check = false;
                }
            }
            cot = 0;
        }
        return win;
    }

    public int checkCheoPhai() {
        int win = 0, cheop = 0, n = 0, k = 0;
        boolean check = false;
        for (int i = x - 1; i >= 0; i--) {
            for (int j = 0; j < y; j++) {
                if (check) {
                    if (matran[n - j][j] == 1) {
                        cheop++;
                        if (cheop > 4) {
                            win = 1;
                            break;
                        }
                        continue;
                    } else {
                        check = false;
                        cheop = 0;
                    }
                }
                if (matran[i][j] == 1) {
                    n = i + j;
                    check = true;
                    cheop++;
                } else {
                    check = false;
                }
            }
            cheop = 0;
            check = false;
        }
        return win;
    }

    public int checkCheoTrai() {
        int win = 0, cheot = 0, n = 0;
        boolean check = false;
        for (int i = 0; i < x; i++) {
            for (int j = y - 1; j >= 0; j--) {
                if (check) {
                    if (matran[n - j - 2 * cheot][j] == 1) {
                        cheot++;
                        System.out.print("+" + j);
                        if (cheot > 4) {
                            win = 1;
                            break;
                        }
                        continue;
                    } else {
                        check = false;
                        cheot = 0;
                    }
                }
                if (matran[i][j] == 1) {
                    n = i + j;
                    check = true;
                    cheot++;
                } else {
                    check = false;
                }
            }
            n = 0;
            cheot = 0;
            check = false;
        }
        return win;
    }

    public void caro(String x, String y) {
        xx = Integer.parseInt(x);
        yy = Integer.parseInt(y);
        // danh dau vi tri danh
        matran[xx][yy] = 1;
        matrandanh[xx][yy] = 1;
        bt[xx][yy].setEnabled(false);
        //bt[xx][yy].setIcon(new ImageIcon("x.png"));
        bt[xx][yy].setBackground(Color.BLACK);

        // Kiem tra thang hay chua
        System.out.println("CheckH:" + checkHang());
        System.out.println("CheckC:" + checkCot());
        System.out.println("CheckCp:" + checkCheoPhai());
        System.out.println("CheckCt:" + checkCheoTrai());
        winner = (checkHang() == 1 || checkCot() == 1 || checkCheoPhai() == 1 || checkCheoTrai() == 1);
        if (checkHang() == 1 || checkCot() == 1 || checkCheoPhai() == 1
                || checkCheoTrai() == 1) {
            setEnableButton(false);
            //thoigian.stop();
            try {
                oos.writeObject("checkwin,123");
            } catch (IOException ex) {
            }
            Object[] options = {"Dong y", "Huy bo"};
            int m = JOptionPane.showConfirmDialog(this,
                    "Ban da thua.Ban co muon choi lai khong?", "Thong bao",
                    JOptionPane.YES_NO_OPTION);
            if (m == JOptionPane.YES_OPTION) {
                //second = 0;
                //minute = 0;
                setVisiblePanel(p);
                newgame();
                try {
                    oos.writeObject("newgame,123");
                } catch (IOException ie) {
                    //
                }
            } else if (m == JOptionPane.NO_OPTION) {
                //thoigian.stop();
            }
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtChat = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtContent = new javax.swing.JTextArea();
        btnSend = new javax.swing.JButton();
        p = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(750, 500));
        setResizable(false);

        jLabel1.setText("Thời Gian");

        txtContent.setColumns(20);
        txtContent.setRows(5);
        jScrollPane1.setViewportView(txtContent);

        btnSend.setText("Send");
        btnSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pLayout = new javax.swing.GroupLayout(p);
        p.setLayout(pLayout);
        pLayout.setHorizontalGroup(
            pLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 424, Short.MAX_VALUE)
        );
        pLayout.setVerticalGroup(
            pLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 430, Short.MAX_VALUE)
        );

        jMenu1.setText("Game");

        jMenuItem2.setText("New Game");
        jMenu1.add(jMenuItem2);

        jMenuItem1.setText("Exit");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Help");

        jMenuItem3.setText("Help");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(txtChat)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSend)
                        .addGap(20, 20, 20))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(68, 68, 68))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 227, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtChat, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSend))
                .addGap(34, 34, 34))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        JOptionPane.showConfirmDialog(this,
                "Luật chơi rất đơn giản bạn chỉ cần 5 ô liên tiếp nhau\n"
                + "Theo hàng ngang hoặc dọc hoặc chéo là bạn đã thắng", "Luật Chơi",
                JOptionPane.CLOSED_OPTION);

    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed
        // TODO add your handling code here:
        String temp = "";
        try {
            temp += "Tôi: " + txtChat.getText() + "\n";
            txtContent.setText(temp);

            oos.writeObject("chat," + txtChat.getText());
            txtContent.setText("");
            //temp = "";
            txtChat.requestFocus();
            txtContent.setVisible(false);
            txtContent.setVisible(true);
        } catch (Exception ex) {

        }
    }//GEN-LAST:event_btnSendActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSend;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel p;
    private javax.swing.JTextField txtChat;
    private javax.swing.JTextArea txtContent;
    // End of variables declaration//GEN-END:variables
}
