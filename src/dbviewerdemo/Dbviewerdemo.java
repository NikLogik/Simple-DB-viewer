
package dbviewerdemo;

import java.awt.BorderLayout;
import java.util.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author nikitorches
 */
public class Dbviewerdemo extends JFrame{
    
    private JTextField connStr;
    
    private JTextField tblName;
    
    private JTable dbInfo;

    public Dbviewerdemo() {
        super("Database Table Viewer");
        setBounds(300, 300, 600, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        JPanel top = new JPanel(new BorderLayout());
    
        JPanel lines = new JPanel();
        connStr = new JTextField(40);
        connStr.setText("Insert URL for your DB connection");
        JLabel lb1 = new JLabel("Connection string:");
        lb1.setLabelFor(connStr);
        lines.add(lb1);
        lines.add(connStr);
        top.add(lines, BorderLayout.NORTH);
        
        lines = new JPanel();
        tblName = new JTextField(25);
        tblName.setText("Write a name of some table from your DB");
        lb1 = new JLabel("File name");
        lb1.setLabelFor(tblName);
        JButton btn = new JButton("Load");
        btn.addActionListener(e -> onLoadButtonClick());
        lines.add(lb1);
        lines.add(tblName);
        lines.add(btn);
        top.add(lines, BorderLayout.SOUTH);
        
        getContentPane().add(top, BorderLayout.NORTH);


        JPanel table = new JPanel(new BorderLayout());
        
        dbInfo = new JTable();
        table.add(dbInfo.getTableHeader(), BorderLayout.NORTH) ;
        table.add(new JScrollPane(dbInfo), BorderLayout.CENTER);
        
        getContentPane().add(table, BorderLayout.CENTER);
    }
    
    private void onLoadButtonClick() {
        try(Connection conn = DriverManager.getConnection(connStr.getText(), "demo", "demo");
        Statement st = conn.createStatement();
        ResultSet set = st.executeQuery("SELECT * FROM " + tblName.getText())){
            ResultSetMetaData rsmd = set.getMetaData();
            int colCnt = rsmd.getColumnCount();
            Object[] colHdrs = new Object[colCnt];
            for(int i = 0; i < colCnt; i++){
                colHdrs[i] = rsmd.getColumnName(i+1);
            }
            
            List<Object[]> rows = new ArrayList<>();
            while(set.next()){
                Object[] r = new Object[colCnt];
                for(int i = 0; i < colCnt; i++){
                    r[i] = set.getObject(i + 1);
                }
                rows.add(r);
            }
            Object[][] rowsArr = rows.toArray(new Object[0][]);
            ((DefaultTableModel)dbInfo.getModel()).setDataVector(rowsArr, colHdrs);
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        new Dbviewerdemo().setVisible(true);
    }
    
}
