import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;

public class Main {
    private static ResourceBundle resourceBundle;

    private static Map<String, Field[]> classField = new HashMap<String, Field[]>();

    public static void main(String[] args) {
        setLocale("en");
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void setLocale(String language) {
        Locale locale = new Locale(language);
        resourceBundle = ResourceBundle.getBundle("app", locale);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame(resourceBundle.getString("app_title"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        // frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        Font customFont = new Font("Coo Hew ", Font.BOLD,14);

        JLabel classLabel = new JLabel(resourceBundle.getString("class_label"));
        classLabel.setFont(customFont);
        JTextField classNameField=new JTextField();
        classNameField.setPreferredSize(new Dimension(200, 25));
        classNameField.setFont(customFont);
        panel.add(classLabel);
        panel.add(classNameField);

        JLabel fieldLabel = new JLabel(resourceBundle.getString("field_label"));
        fieldLabel.setFont(customFont);
        JComboBox<String> fieldComboBox = new JComboBox<>();
        fieldComboBox.setFont(customFont);
        panel.add(fieldLabel);
        panel.add(fieldComboBox);

        JLabel paramLabel = new JLabel(resourceBundle.getString("param_label"));
        paramLabel.setFont(customFont);
        JTextField paramTextField = new JTextField();
        paramTextField.setColumns(10);
        paramTextField.setFont(customFont);
        panel.add(paramLabel);
        panel.add(paramTextField);

        JButton executeButton = new JButton(resourceBundle.getString("execute_button"));
        executeButton.setFont(customFont);
        JLabel resultLabel = new JLabel(resourceBundle.getString("result_label"));
        resultLabel.setFont(customFont);
        JTextArea resultText = new JTextArea();
        resultText.setFont(customFont);
        resultText.setRows(3);
        resultText.setEditable(false);

        panel.add(Box.createRigidArea(new Dimension(10, 10)));
        panel.add(executeButton);
        panel.add(resultLabel);
        panel.add(new JScrollPane(resultText));

        JComboBox<String> languageComboBox = new JComboBox<>(new String[]{"English", "Русский"});
        JLabel chooseLabel = new JLabel(resourceBundle.getString("choose_label"));
        chooseLabel.setFont(customFont);

        panel.add(Box.createRigidArea(new Dimension(10, 10)));
        panel.add(chooseLabel);
        panel.add(languageComboBox);
        classNameField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String className =classNameField.getText();
                if ("Experiment".equals(className)) {
                    if (!classField.containsKey(className)) {
                        Field[] fields = Experiment.class.getDeclaredFields();
                        classField.put(className, fields);
                    }
                    populateFieldComboBox(fieldComboBox, classField.get(className));
                } else if ("Exp".equals(className)) {
                    if (!classField.containsKey(className)) {
                        Field[] fields = Exp.class.getDeclaredFields();
                        classField.put(className, fields);
                    }
                    populateFieldComboBox(fieldComboBox, classField.get(className));
                } else {
                    fieldComboBox.removeAllItems();
                }
            }
        });

        executeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String className =  classNameField.getText();
                String fieldName = (String) fieldComboBox.getSelectedItem();
                String parameter = paramTextField.getText();
                //fieldName=parameter;
                try {
                    if ("Experiment".equals(className)) {
                        if ("timeZone".equals(fieldName)) {
                            // Выполнение метода Experiment
                            Experiment experiment = new Experiment("GMT", "USD", "EUR", 100.0);
                            String selectedLanguage = (String) languageComboBox.getSelectedItem();
                            if ("English".equals(selectedLanguage)) {
                                resultText.setText(executeExperiment(experiment, "showTimeInLondon", parameter));
                            }
                            else if ("Русский".equals(selectedLanguage)) {
                                resultText.setText(executeExperiment(experiment, "showTimeInBelarus", parameter));
                            }


                        } else if ("amount".equals(fieldName)) {
                            // Выполнение метода Experiment
                            double amount = Double.parseDouble(parameter);
                            Experiment experiment = new Experiment("GMT", "BYN", "USD", amount);
                            Properties properties = new Properties();
                            FileInputStream fileInputStream = new FileInputStream("src\\value.properties");
                            properties.load(fileInputStream);

                            double usdRate = Double.parseDouble(properties.getProperty("USD"));
                            String selectedLanguage = (String) languageComboBox.getSelectedItem();
                            if ("English".equals(selectedLanguage)) {
                                resultText.setText(executeExperiment(experiment, "showUSDtoBYN", usdRate, Double.parseDouble(String.valueOf(amount))));
                            } else if ("Русский".equals(selectedLanguage)) {
                                resultText.setText(executeExperiment(experiment, "showBYNtoUSD", usdRate, Double.parseDouble(String.valueOf(amount))));
                            }

                        }
                    } else {
//                        if("Exp".equals(className)) {
//                            if("money".equals(fieldName)){
//                            Exp money=new Exp();
//                            money.SetMoney(parameter);
//                            }else if("time".equals(fieldName)){
//
//                            }
//                        }
                        resultText.setText("Field changed");
                    }
                } catch (Exception ex) {
                    resultText.setText("Error: " + ex.getMessage());
                }
            }
        });



        languageComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedLanguage = (String) languageComboBox.getSelectedItem();
                if ("English".equals(selectedLanguage)) {
                    setLocale("en");
                } else if ("Русский".equals(selectedLanguage)) {
                    setLocale("ru");
                }
                frame.setTitle(resourceBundle.getString("app_title"));
                classLabel.setText(resourceBundle.getString("class_label"));
                fieldLabel.setText(resourceBundle.getString("field_label"));
                paramLabel.setText(resourceBundle.getString("param_label"));
                executeButton.setText(resourceBundle.getString("execute_button"));
                resultLabel.setText(resourceBundle.getString("result_label"));
                chooseLabel.setText(resourceBundle.getString("choose_label"));
            }
        });

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private static void populateFieldComboBox(JComboBox<String> comboBox, Field[] fields) {
        comboBox.removeAllItems();
        for (Field field : fields) {
            comboBox.addItem(field.getName());
        }
    }

    private static String executeExperiment(Experiment experiment, String methodName, String timeZoneDifference) {
        try {
            Method method = Experiment.class.getMethod(methodName, String.class);
            return (String) method.invoke(experiment, timeZoneDifference);
        } catch (Exception ex) {
            return "Error: " + ex.getMessage();
        }
    }

    private static String executeExperiment(Experiment experiment, String methodName, double conversionRate, double amount) {
        try {
            Method method = Experiment.class.getMethod(methodName, double.class, double.class);
            return (String) method.invoke(experiment, conversionRate, amount);
        } catch (Exception ex) {
            return "Error: " + ex.getMessage();
        }
    }

}