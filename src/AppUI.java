import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Random;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
public class AppUI {
    JFrame f;
    JPanel main;
    JTextField calcField;
    JTextArea noteArea;
    JLabel clockLabel;
    JTextField passField;
    public AppUI() {
        f = new JFrame("Utility Hub");
        main = new JPanel();
        main.setLayout(new GridLayout(2,3,20,20));
        main.setBackground(new Color(30,30,30));
        main.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        addBtn("Calculator", new Color(155,89,182));
        addBtn("Notepad", new Color(241,196,15));
        addBtn("Clock", new Color(231,76,60));
        addBtn("Password Gen", new Color(26,188,156));
        addBtn("Password Save", new Color(52,152,219));
        addBtn("Converter", new Color(46,204,113));
        f.add(main);
        f.setSize(650,500);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
    void addBtn(String name, Color c){
        JButton b = new JButton(name);
        b.setFont(new Font("Arial",Font.BOLD,18));
        b.setFocusPainted(false);
        b.setBackground(c);
        b.setForeground(Color.white);

        b.addActionListener(e -> open(name));

        main.add(b);
    }
    void open(String name){
        if(name.equals("Calculator")) calcUI();
        if(name.equals("Notepad")) noteUI();
        if(name.equals("Clock")) clockUI();
        if(name.equals("Password Gen")) passUI();
        if(name.equals("Password Save")) saveUI();
        if(name.equals("Converter")) convUI();
    }
    void calcUI(){
        JFrame x = new JFrame("Calculator");
        x.setSize(300,400);
        x.setLocationRelativeTo(null);
        JPanel p = new JPanel(new BorderLayout());
        calcField = new JTextField();
        p.add(calcField, BorderLayout.NORTH);
        JPanel g = new JPanel(new GridLayout(4,4));
        String[] b = {"7","8","9","/","4","5","6","*","1","2","3","-","0","=","C","+"};
        for(String s:b){
            JButton bt = new JButton(s);
            bt.addActionListener(e -> press(s));
            g.add(bt);
        }
        p.add(g);
        JButton back = new JButton("Back");
        back.addActionListener(e -> x.dispose());
        p.add(back, BorderLayout.SOUTH);
        x.add(p);
        x.setVisible(true);
    }
    void press(String s){
        if(s.equals("=")){
            try{
                calcField.setText(""+eval(calcField.getText()));
            }catch(Exception e){
                calcField.setText("Error");
            }
        } else if(s.equals("C")){
            calcField.setText("");
        } else {
            calcField.setText(calcField.getText()+s);
        }
    }
    int eval(String exp){
        return (int)new Object(){
            int pos=-1,ch;
            void next(){ ch=(++pos<exp.length())?exp.charAt(pos):-1; }
            boolean eat(int c){
                while(ch==' ') next();
                if(ch==c){ next(); return true;}
                return false;
            }
            int parse(){ next(); return expr(); }
            int expr(){
                int x=term();
                for(;;){
                    if(eat('+')) x+=term();
                    else if(eat('-')) x-=term();
                    else return x;
                }
            }
            int term(){
                int x=factor();
                for(;;){
                    if(eat('*')) x*=factor();
                    else if(eat('/')) x/=factor();
                    else return x;
                }
            }
            int factor(){
                if(eat('+')) return factor();
                if(eat('-')) return -factor();
                int x,start=this.pos;
                if((ch>='0'&&ch<='9')){
                    while((ch>='0'&&ch<='9')) next();
                    x=Integer.parseInt(exp.substring(start,this.pos));
                } else x=0;
                return x;
            }
        }.parse();
    }
    void noteUI(){
        JFrame x = new JFrame("Notepad");
        x.setSize(400,400);
        x.setLocationRelativeTo(null);
        JPanel p = new JPanel(new BorderLayout());
        noteArea = new JTextArea();
        JScrollPane sp = new JScrollPane(noteArea);
        JButton save = new JButton("Save");
        JButton clear = new JButton("Clear");
        JPanel b = new JPanel();
        b.add(save);
        b.add(clear);
        save.addActionListener(e -> {
            try{
                FileWriter w = new FileWriter("note.txt");
                w.write(noteArea.getText());
                w.close();
                JOptionPane.showMessageDialog(x,"Saved");
            }catch(Exception ex){}
        });
        clear.addActionListener(e -> noteArea.setText(""));
        JButton back = new JButton("Back");
        back.addActionListener(e -> x.dispose());
        b.add(back);
        p.add(sp);
        p.add(b,BorderLayout.SOUTH);
        x.add(p);
        x.setVisible(true);
    }
    void clockUI(){
        JFrame x = new JFrame("Clock");
        x.setSize(300,200);
        x.setLocationRelativeTo(null);
        clockLabel = new JLabel("",SwingConstants.CENTER);
        clockLabel.setFont(new Font("Arial",Font.BOLD,30));
        x.add(clockLabel);
        Timer t = new Timer(1000, e -> updateClock());
        t.start();
        JButton back = new JButton("Back");
        back.addActionListener(e -> x.dispose());
        x.add(back, BorderLayout.SOUTH);

        x.setVisible(true);
    }
    void updateClock(){
        LocalTime t = LocalTime.now();
        DateTimeFormatter f = DateTimeFormatter.ofPattern("HH:mm:ss");
        clockLabel.setText(t.format(f));
    }
    void passUI(){
        JFrame x = new JFrame("Password Generator");
        x.setSize(300,200);
        x.setLocationRelativeTo(null);
        JPanel p = new JPanel();
        passField = new JTextField(15);
        JButton gen = new JButton("Generate");
        gen.addActionListener(e -> passField.setText(makePass()));
        p.add(passField);
        p.add(gen);
        JButton back = new JButton("Back");
        back.addActionListener(e -> x.dispose());
        p.add(back);
        x.add(p);
        x.setVisible(true);
    }
    String makePass(){
        String s="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#";
        Random r=new Random();
        String out="";
        for(int i=0;i<8;i++){
            out+=s.charAt(r.nextInt(s.length()));
        }
        return out;
    }
    void saveUI(){
        JFrame login = new JFrame("Login");
        login.setSize(300,200);
        login.setLocationRelativeTo(null);
        JPanel p = new JPanel();
        JPasswordField pf = new JPasswordField(15);
        JButton b = new JButton("Enter");
        p.add(new JLabel("Master Password"));
        p.add(pf);
        p.add(b);
        login.add(p);
        login.setVisible(true);
        b.addActionListener(e -> {
            String input = new String(pf.getPassword());
            try{
                File f = new File("master.txt");
                if(!f.exists()){
                    FileWriter w = new FileWriter("master.txt");
                    w.write(input);
                    w.close();
                    login.dispose();
                    managerUI();
                } else {
                    BufferedReader r = new BufferedReader(new FileReader(f));
                    String saved = r.readLine();
                    if(input.equals(saved)){
                        login.dispose();
                        managerUI();
                    }
                }
            }catch(Exception ex){}
        });
    }
    void managerUI(){
        JFrame x = new JFrame("Password Saver");
        x.setSize(400,400);
        x.setLocationRelativeTo(null);
        JPanel p = new JPanel(new BorderLayout());
        JPanel top = new JPanel(new GridLayout(2,2,10,10));
        JTextField acc = new JTextField();
        JTextField pass = new JTextField();
        top.add(new JLabel("Account"));
        top.add(new JLabel("Password"));
        top.add(acc);
        top.add(pass);
        JPanel mid = new JPanel();
        JButton save = new JButton("Save");
        JButton load = new JButton("Load");
        mid.add(save);
        mid.add(load);
        JTextArea area = new JTextArea();
        area.setEditable(false);
        JScrollPane sp = new JScrollPane(area);
        JPanel bottom = new JPanel();
        JButton back = new JButton("Back");
        bottom.add(back);
        save.addActionListener(e -> {
            try{
                File file = new File(System.getProperty("user.dir") + "/data.txt");
                FileWriter w = new FileWriter(file, true);
                w.write(acc.getText()+" : "+pass.getText()+"\n");
                w.close();
                JOptionPane.showMessageDialog(x,"Saved");
                acc.setText("");
                pass.setText("");
            }catch(Exception ex){}
        });
        load.addActionListener(e -> {
            try{
                File file = new File(System.getProperty("user.dir") + "/data.txt");
                if(!file.exists()){
                    area.setText("No data found");
                    return;
                }
                BufferedReader r = new BufferedReader(new FileReader(file));
                String line;
                area.setText("");
                while((line = r.readLine()) != null){
                    area.append(line+"\n");
                }
                r.close();
            }catch(Exception ex){}
        });
        back.addActionListener(e -> x.dispose());
        JPanel down = new JPanel(new BorderLayout());
        down.add(sp, BorderLayout.CENTER);
        down.add(bottom, BorderLayout.SOUTH);
        p.add(top, BorderLayout.NORTH);
        p.add(mid, BorderLayout.CENTER);
        p.add(down, BorderLayout.SOUTH);
        x.add(p);
        x.setVisible(true);
    }
    void convUI(){
        JFrame x = new JFrame("Converter");
        x.setSize(300,200);
        x.setLocationRelativeTo(null);
        JPanel p = new JPanel();
        JTextField in = new JTextField(10);
        String[] opt = {"m to km","kg to g","C to F"};
        JComboBox<String> box = new JComboBox<>(opt);

        JButton b = new JButton("Convert");
        JLabel out = new JLabel("");
        b.addActionListener(e -> {
            try{
                double v = Double.parseDouble(in.getText());
                String s = (String)box.getSelectedItem();

                if(s.equals("m to km")) out.setText(""+(v/1000));
                if(s.equals("kg to g")) out.setText(""+(v*1000));
                if(s.equals("C to F")) out.setText(""+((v*9/5)+32));
            }catch(Exception ex){}
        });
        p.add(in);
        p.add(box);
        p.add(b);
        p.add(out);

        JButton back = new JButton("Back");
        back.addActionListener(e -> x.dispose());
        p.add(back);

        x.add(p);
        x.setVisible(true);
    }
    public static void main(String[] args) {
        new AppUI();
    }
}