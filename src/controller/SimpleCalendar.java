package controller;

import com.github.lgooddatepicker.components.CalendarPanel;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.optionalusertools.CalendarSelectionListener;
import com.github.lgooddatepicker.optionalusertools.DateHighlightPolicy;
import com.github.lgooddatepicker.zinternaltools.CalendarSelectionEvent;
import com.github.lgooddatepicker.zinternaltools.HighlightInformation;
import listeners.EventDetialsListener;
import listeners.NewEventCreateListener;
import model.JSONModelParser;
import model.ScheduleModel;
import view.CustomCreateDialog;
import view.CustomViewDialog;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * Created by arup3 on 5/11/2017.
 */
public class SimpleCalendar extends JPanel {

    static CalendarPanel calendarPanel;
    static JPanel panel, divider, txtPanel;
    static JFrame frame;
    static JButton buttonCreate,buttonExit;
    static JList<String> eventList;
    static ArrayList<ScheduleModel> modelList;
    static DefaultListModel<String> titles;
    static ArrayList<Integer> daysList;
    static ArrayList<Integer> monthsList;
    static ArrayList<Integer> yearList;

    static FileUtils utils;
    static JSONModelParser parser;

    public static void main(String[] args) {
        initObjects();
        initLists();
        initFramePanel();
        initViewObj();
        initExtraObj();
        setListeners();
        setPanel();

    }
    private static void initObjects(){
        modelList = new ArrayList<>();
        titles = new DefaultListModel<>();
        daysList = new ArrayList<>();
        yearList = new ArrayList<>();
        monthsList = new ArrayList<>();
        parser = new JSONModelParser();
        utils = new FileUtils();
    }

    private static void initLists(){
        String jsons = utils.readFile();
        System.out.println("jsons: " + jsons);
        modelList.addAll(parser.parseJson(jsons));
        System.out.println("size: "+modelList.size());

        for (int i = 0; i < modelList.size(); i++) {
            daysList.add(modelList.get(i).getDay());
            System.out.println("day "+i+" : "+modelList.get(i).getDay());
        }
        System.out.println("total days: "+daysList.size());

        for (int i = 0; i < modelList.size(); i++) {
            monthsList.add(modelList.get(i).getMonth());
            System.out.println("month "+i+" : "+modelList.get(i).getMonth());
        }
        System.out.println("total months: "+monthsList.size());

        for (int i = 0; i < modelList.size(); i++) {
            yearList.add(modelList.get(i).getYear());
            System.out.println("month "+i+" : "+modelList.get(i).getYear());
        }
        System.out.println("total months: "+yearList.size());
    }

    private static void initFramePanel() {
        frame = new JFrame("Event Scheduler");
        panel = new JPanel();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(600, 600));
        frame.setResizable(false);
    }

    private static void removeItem(int position,String key){
        DefaultListModel model = (DefaultListModel) eventList.getModel();
        int selectedIndex =eventList.getSelectedIndex();
        if (selectedIndex != -1) {
            int indexOfMainModel = getIndex(titles.get(selectedIndex));
            model.remove(selectedIndex);
            eventList.revalidate();
            if(indexOfMainModel!=-1){
                System.out.println("clicked position: "+indexOfMainModel);
                modelList.remove(indexOfMainModel);
                utils.rewriteJson(utils.createJosn(modelList));
            }
        }
//        SimpleCalendar.titles.removeElementAt(position);
    }

    private static void updateItem(int position,String title,String starttime,String endTime){
        DefaultListModel model = (DefaultListModel) eventList.getModel();
        int selectedIndex =eventList.getSelectedIndex();
        if (selectedIndex != -1) {
            int indexOfMainModel = getIndex(titles.get(selectedIndex));
            titles.remove(selectedIndex);
            titles.add(selectedIndex,title);
            eventList.revalidate();
            if(indexOfMainModel!=-1){
                System.out.println("clicked position: "+indexOfMainModel);
                modelList.get(indexOfMainModel).setTitle(title);
                modelList.get(indexOfMainModel).setEndTime(endTime);
                modelList.get(indexOfMainModel).setStartTime(starttime);
                utils.rewriteJson(utils.createJosn(modelList));
            }
        }
//        SimpleCalendar.titles.removeElementAt(position);
    }

    private static void setPanel() {

        eventList = new JList<>(titles);
        eventList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        eventList.addListSelectionListener(new ListSelectionListener() {
            int position=0;
            @Override
            public void valueChanged(ListSelectionEvent e) {
                position = e.getLastIndex();
                final ArrayList<String> selectedValuesList = new ArrayList<>();
                selectedValuesList.addAll(eventList.getSelectedValuesList());
                System.out.println(selectedValuesList);
                eventList.revalidate();
                int selectedIndex = eventList.getSelectedIndex();
                if (selectedIndex != -1) {
                    int modelIndex = getIndex(titles.get(selectedIndex));
                    CustomViewDialog viewDialog = new CustomViewDialog(frame, true,
                            selectedValuesList.get(0),
                            modelList.get(modelIndex).getStartTime(),
                            modelList.get(modelIndex).getEndTime(),
                             new
                            EventDetialsListener() {
                                @Override
                                public void onUpdate(String title, String startTime, String endTime) {
                                    System.out.println("position: " + position);
                                    System.out.println("title "+title+" start: "+startTime+" end: "+endTime);
                                    updateItem(position,title,startTime,endTime);
                                }

                                @Override
                                public void onDelete() {
                                    SimpleCalendar.removeItem(position, "");
                                }
                            });
                }
            }
        });

        Dimension dim = new Dimension(600, 300);
        eventList.setSize(dim);
        System.out.println("size is:" + titles.size());


        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.3;

        gbc.fill = GridBagConstraints.BOTH;
        panel.add(buttonExit, gbc);

        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.CENTER;
        panel.add(calendarPanel, gbc);

        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(buttonCreate, gbc);

//        gbc.gridy = 2;
//        gbc.fill = GridBagConstraints.BOTH;
//        panel.add(divider,gbc);

        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(new JScrollPane(eventList), gbc);

        frame.setContentPane(panel);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }


    private static void initExtraObj() {
        DatePickerSettings dateSettings = new DatePickerSettings(new Locale("en"));
        dateSettings.setHighlightPolicy(new SimpleHighlightPolicy());
        dateSettings.setSizeDatePanelMinimumWidth(400);
        dateSettings.setSizeDatePanelMinimumHeight(250);
        calendarPanel.setSettings(dateSettings);
    }

    private static void initViewObj() {
        calendarPanel = new CalendarPanel();
        divider = new JPanel();
        txtPanel = new JPanel();
        buttonCreate = new JButton("New Event");
        buttonExit = new JButton("Exit");
        buttonCreate.setFont(new Font("Arial", Font.PLAIN, 40));
        Dimension d = new Dimension(0, 40);
        buttonCreate.setPreferredSize(d);

        Dimension dim = new Dimension(0, 5);
        divider.setPreferredSize(dim);
        divider.setBackground(Color.black);
    }

    private static void setListeners() {

        buttonCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("clicked on button ");
                //todo open a new dialog box to get user entry
                getNewInput();
            }
        });
        buttonExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("clicked on button exit");
                System.exit(1);
            }
        });


        calendarPanel.addCalendarSelectionListener(new CalendarSelectionListener() {
            @Override
            public void selectionChanged(CalendarSelectionEvent calendarSelectionEvent) {
//                System.out.println("selected");
//                System.out.println("mew date: " + calendarSelectionEvent.getNewDate());

                checkDataForThisDay(calendarSelectionEvent);
            }
        });
    }

    private static void checkDataForThisDay(CalendarSelectionEvent mEvent) {
        titles.clear();
        for (int i = 0; i < modelList.size(); i++) {
            if (modelList.get(i).getMonth() == mEvent.getNewDate().getMonth().getValue()) {
                for(int j=0;j< modelList.size();j++) {
                    if (modelList.get(j).getDay() == mEvent.getNewDate().getDayOfMonth() && modelList.get(j).getMonth() == modelList.get(i).getMonth()) {
                        //data exists
                        System.out.println("date: "+mEvent.getNewDate().getDayOfMonth()+"  m: "+mEvent.getNewDate().getMonthValue());
                        titles.addElement(modelList.get(j).getTitle());
                    }
                }
                return;
            }
        }
    }

    private static void getNewInput() {
        //if no date is selected by user then auto select the current date
        if(calendarPanel.getSelectedDate() == null){
            Date date=new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
            simpleDateFormat.applyPattern("MMM");
            String month = simpleDateFormat.format(date);
            System.out.println("Month : " + simpleDateFormat.format(date));


            Calendar cal = Calendar.getInstance();
//            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int year = cal.get(Calendar.YEAR);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd");
            formatter = formatter.withLocale( Locale.US );  // Locale specifies human language for translating, and cultural norms for lowercase/uppercase and abbreviations and such. Example: Locale.US or Locale.CANADA_FRENCH
            String dateToFormat = (year + "-"+month+"-"+day);
            LocalDate local = LocalDate.parse(dateToFormat, formatter);
            calendarPanel.setSelectedDate(local);
        }

        int month = calendarPanel.getSelectedDate().getMonthValue();
        int day = calendarPanel.getSelectedDate().getDayOfMonth();
        int year = calendarPanel.getSelectedDate().getYear();
        CustomCreateDialog dialog = new CustomCreateDialog(frame, true, "Create new event for "+year+"/"+month+"/"+day,
                new NewEventCreateListener() {
                    @Override
                    public void onEventCreated(String title, String startTime, String endTime) {
                        ScheduleModel model = new ScheduleModel();
                        model.setYear(year);
                        model.setEndTime(endTime);
                        model.setStartTime(startTime);
                        model.setTitle(title);
                        model.setDay(day);
                        model.setMonth(month);

                        if(doesDataExistsByTime(year,month,day,startTime)){
                            System.out.println("already exists");
                            JOptionPane optionPane = new JOptionPane("There is a time conflict. Choose a different start time",JOptionPane.WARNING_MESSAGE);
                            JDialog dialog = optionPane.createDialog("Error!");
                            dialog.setAlwaysOnTop(true); // to show top of all other application
                            dialog.setVisible(true);
                        }else{
                            titles.addElement(title);
                            modelList.add(model);
                            utils.rewriteJson(utils.createJosn(modelList));
                        }
                    }
                });
        if (dialog.getAnswer()) {
            System.err.println("The answer stored in view.CustomCreateDialog is 'true' (i.e. user clicked yes button.)");
        } else {
            System.err.println("The answer stored in view.CustomCreateDialog is 'false' (i.e. user clicked no button.)");
        }


    }

    private static int getIndex(String title){
        int month = calendarPanel.getSelectedDate().getMonthValue();
        int day = calendarPanel.getSelectedDate().getDayOfMonth();
        int year = calendarPanel.getSelectedDate().getYear();

        int index = -1;

        for(int i =0;i<modelList.size();i++){
            if(modelList.get(i).getYear()==year){
                for(int j=0;j<modelList.size();j++){
                    if(modelList.get(j).getMonth()==month && modelList.get(j).getYear()==modelList.get(i).getYear()){
                        for(int k=0;k<modelList.size();k++){
                            if(modelList.get(k).getDay() == day && modelList.get(k).getMonth()==modelList.get(j).getMonth()){
                                for(int l=0;l<modelList.size();l++){
                                    if(modelList.get(l).getTitle().equals(title) && (modelList.get(l).day == modelList.get(k).getDay())){
                                        index = l;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
            return index;
    }

    private static boolean doesDataExistsByCalendar(int year, int month, int day){
        boolean exists  = false;
        for(int i =0;i<modelList.size();i++){
            if(modelList.get(i).getYear()==year){
                for(int j=0;j<modelList.size();j++){
                    if(modelList.get(j).getMonth()==month && modelList.get(j).getYear()==modelList.get(i).getYear()){
                        for(int k=0;k<modelList.size();k++){
                            if(modelList.get(k).getDay() == day && modelList.get(k).getMonth()==modelList.get(j).getMonth()){
                                exists = true;
                                return  exists;
                            }
                        }
                    }
                }
            }
        }

        return exists;
    }


    private static boolean doesDataExistsByTime(int year, int month, int day,String startTime){
        boolean exists  = false;
        for(int i =0;i<modelList.size();i++){
            if(modelList.get(i).getYear()==year){
                for(int j=0;j<modelList.size();j++){
                    if(modelList.get(j).getMonth()==month&& modelList.get(j).getYear()==modelList.get(i).getYear()){
                        for(int k=0;k<modelList.size();k++){
                            if(modelList.get(k).getDay() == day && modelList.get(k).getMonth()==modelList.get(j).getMonth()){
                                for(int l=0;l<modelList.size();l++){
                                    if(modelList.get(l).getStartTime().equals(startTime) && (modelList.get(l).getDay()==modelList.get(k).getDay())){
                                        exists = true;
                                        return  exists;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return exists;
    }


    ///-----------------------------------
    private static class SimpleHighlightPolicy implements DateHighlightPolicy {

        @Override
        public HighlightInformation getHighlightInformationOrNull(LocalDate date) {
            // Highlight a chosen date, with a tooltip and a red background color.
//            if (monthsList.contains(date.getMonthValue())) {
//                int indexOfMonth = monthsList.indexOf(date.getMonthValue());
//                if (modelList.get(indexOfMonth).getDay() == (date.getDayOfMonth())) {
//                    System.out.println( " month: "+date.getMonthValue()+"  day: "+date.getDayOfMonth());
//                    return new HighlightInformation(Color.blue, Color.white, "Selct and click add new");
//                }
//            }

            if (doesDataExistsByCalendar(date.getYear(),date.getMonthValue(),date.getDayOfMonth())) {
                return new HighlightInformation(Color.blue, Color.white, "Selct and click add new");
            }

            DateFormat inputDF  = new SimpleDateFormat("MM/dd/yy");
            Calendar cal = Calendar.getInstance();
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
//            System.out.println("month is: "+month);

            if(month == date.getMonthValue() && day == date.getDayOfMonth()){
                return new HighlightInformation(Color.pink, Color.white, "Todays date");

            }
            return null;
        }


    }
}