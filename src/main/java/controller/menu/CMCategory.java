package controller.menu;

import controller.classroom.CClassroomList;
import controller.klass.CClassList;
import controller.lecture.CLectureList;
import controller.lesson.CLessonList;
import controller.school.CSchoolDetail;
import controller.subject.CSubjectList;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.awt.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.AbstractModel;
import model.custom.java.util.SwarmObserver;
import model.database.component.DBAvailability;
import model.database.component.DBClass;
import model.database.component.DBClassroom;
import model.database.component.DBDay;
import model.database.component.DBLecture;
import model.database.component.DBPeriod;
import model.database.component.DBSchool;
import model.database.component.DBSubject;
import model.database.component.DBTimeOff;
import model.database.component.metadata.DBMClass;
import model.database.component.metadata.DBMClassroom;
import model.database.component.metadata.DBMDay;
import model.database.component.metadata.DBMLecture;
import model.database.component.metadata.DBMLesson;
import model.database.component.metadata.DBMPeriod;
import model.database.component.metadata.DBMSchool;
import model.database.component.metadata.DBMSubject;
import model.database.core.DBType;
import model.database.loader.DBProblemLoader;
import model.database.model.MSchool;
import model.dataset.component.DSLesson;
import model.dataset.component.DSLessonCluster;
import model.dataset.component.DSTimeOff;
import model.dataset.component.DSTimeOffPlacement;
import model.dataset.loader.DatasetGenerator;
import model.method.pso.hdpso.component.Data;
import model.method.pso.hdpso.component.Particle;
import model.method.pso.hdpso.component.PlacementProperty;
import model.method.pso.hdpso.component.Setting;
import model.method.pso.hdpso.core.PSO;
import model.util.Session;
import model.util.list.IntHList;
import org.apache.commons.math3.util.FastMath;
import org.controlsfx.control.Notifications;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import view.classroom.IClassroomList;
import view.klass.IClassList;
import view.lecture.ILectureList;
import view.lesson.ILessonList;
import view.school.ISchoolDetail;
import view.subject.ISubjectList;

/*
 * This <skripsi.hdpso.scheduling> project in package <controller.menu> created by :
 * Name         : syafiq
 * Date / Time  : 01 January 2017, 4:29 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings({"unused", "FieldCanBeLocal", "WeakerAccess"})
public class CMCategory implements Initializable
{
    @NotNull final BooleanProperty generateListener;
    @Nullable
    private final  Observer        content;
    @FXML
    public         Button          mc_button_generate;
    @FXML
    public         Button          bSchool;
    @FXML
    public         Button          bSubject;
    @FXML
    public         Button          bClass;
    @FXML
    public         Button          bClassroom;
    @FXML
    public         Button          bLecture;
    @FXML
    public         Button          bLesson;
    @NotNull       SwarmObserver   swarmObserver;
    @Nullable
    private        DBMSchool       schoolMetadata;

    @Nullable
    private List<DBMDay> dayMetadata;

    @Nullable
    private List<DBMPeriod> periodMetadata;

    @Nullable
    private List<DBAvailability> availability;

    @Nullable
    private List<DBMSubject> subjectMetadata;

    @Nullable
    private List<DBMClass> classMetadata;

    @Nullable
    private List<DBMClassroom> classroomMetadata;

    @Nullable
    private List<DBMLecture> lectureMetadata;

    @Nullable
    private List<DBMLesson> lessonMetadata;


    public CMCategory()
    {
        this(null);
    }


    @SuppressWarnings("unchecked")
    public CMCategory(@Nullable Observer rootContentCallback)
    {
        this.content = rootContentCallback;
        this.setData();
        this.generateListener = new SimpleBooleanProperty(false);
        this.swarmObserver = particles ->
        {
        };
    }

    @SuppressWarnings("unchecked")
    private void setData()
    {
        final Session session = Session.getInstance();
        this.schoolMetadata = (DBMSchool) session.get("school");
        this.dayMetadata = (List<DBMDay>) session.get("day");
        this.periodMetadata = (List<DBMPeriod>) session.get("period");
        this.availability = (List<DBAvailability>) session.get("availability");
        this.subjectMetadata = (List<DBMSubject>) session.get("subject");
        this.classMetadata = (List<DBMClass>) session.get("klass");
        this.classroomMetadata = (List<DBMClassroom>) session.get("classroom");
        this.lectureMetadata = (List<DBMLecture>) session.get("lecture");
        this.lessonMetadata = (List<DBMLesson>) session.get("lesson");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        this.bindSchoolData();

    }

    private void bindSchoolData()
    {
        @NotNull final BooleanProperty schoolExistenceListener = new SimpleBooleanProperty(true);
        @NotNull final Observer schoolExistenceObserver = (o, arg) ->
        {
            schoolExistenceListener.setValue(!Session.getInstance().containsKey("school"));
            if(arg != null)
            {
                if(arg instanceof String)
                {
                    if(((String) arg).contentEquals("school"))
                    {
                        CMCategory.this.setData();
                    }
                }
            }
        };
        Session.getInstance().addObserver(schoolExistenceObserver);
        this.mc_button_generate.disableProperty().bind(schoolExistenceListener);
        this.bSchool.disableProperty().bind(schoolExistenceListener);
        this.bSubject.disableProperty().bind(schoolExistenceListener);
        this.bClass.disableProperty().bind(schoolExistenceListener);
        this.bClassroom.disableProperty().bind(schoolExistenceListener);
        this.bLecture.disableProperty().bind(schoolExistenceListener);
        this.bLesson.disableProperty().bind(schoolExistenceListener);
    }

    public void onSchoolButtonPressed(ActionEvent actionEvent)
    {
        if((this.schoolMetadata != null) && (this.dayMetadata != null) && (this.periodMetadata != null))
        {
            @NotNull final Stage dialog = new Stage();
            dialog.setTitle("Detail Jadwal");

            try
            {

                @NotNull final AbstractModel model  = new MSchool(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
                @NotNull final DBSchool      school = MSchool.getFromMetadata(model, this.schoolMetadata);
                dialog.setScene(new Scene(ISchoolDetail.load(new CSchoolDetail(school, this.dayMetadata, this.periodMetadata)
                {
                    @Override
                    public void schoolUpdated(String name, String nickname, String address, String academicYear, int semester, int activeDay, int activePeriod)
                    {
                        if(CMCategory.this.schoolMetadata != null)
                        {
                            CMCategory.this.schoolMetadata.setName(name);
                            CMCategory.this.schoolMetadata.setAcademicYear(academicYear);
                            CMCategory.this.schoolMetadata.setSemester(semester);
                            CMCategory.this.schoolMetadata.setActiveDay(activeDay);
                            CMCategory.this.schoolMetadata.setActivePeriod(activePeriod);
                        }
                        super.schoolUpdated(name, nickname, address, academicYear, semester, activeDay, activePeriod);
                    }
                }).load()));
            }
            catch(IOException | SQLException ignored)
            {
                ignored.printStackTrace();
            }

            dialog.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.showAndWait();
        }
    }

    public void onSubjectButtonPressed(ActionEvent actionEvent)
    {
        if((this.schoolMetadata != null) && (this.dayMetadata != null) && (this.periodMetadata != null) && (this.subjectMetadata != null) && (this.availability != null))
        {
            @NotNull final Stage dialog = new Stage();
            dialog.setTitle("Daftar Mata Kuliah");
            try
            {
                dialog.setScene(new Scene(ISubjectList.load(new CSubjectList(this.schoolMetadata, this.dayMetadata, this.periodMetadata, this.availability, this.subjectMetadata)).load()));
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            dialog.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.showAndWait();
        }
    }

    public void onClassButtonPressed(ActionEvent actionEvent)
    {
        if((this.schoolMetadata != null) && (this.dayMetadata != null) && (this.periodMetadata != null) && (this.classMetadata != null) && (this.availability != null))
        {
            @NotNull final Stage dialog = new Stage();
            dialog.setTitle("Daftar Kelas");
            try
            {
                dialog.setScene(new Scene(IClassList.load(new CClassList(this.schoolMetadata, this.dayMetadata, this.periodMetadata, this.availability, this.classMetadata)).load()));
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            dialog.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.showAndWait();
        }
    }

    public void onClassroomButtonPressed(ActionEvent actionEvent)
    {
        if((this.schoolMetadata != null) && (this.dayMetadata != null) && (this.periodMetadata != null) && (this.classroomMetadata != null) && (this.availability != null))
        {
            @NotNull final Stage dialog = new Stage();
            dialog.setTitle("Daftar Ruangan");
            try
            {
                dialog.setScene(new Scene(IClassroomList.load(new CClassroomList(this.schoolMetadata, this.dayMetadata, this.periodMetadata, this.availability, this.classroomMetadata)).load()));
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            dialog.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.showAndWait();
        }
    }

    public void onLectureButtonPressed(ActionEvent actionEvent)
    {
        if((this.schoolMetadata != null) && (this.dayMetadata != null) && (this.periodMetadata != null) && (this.lectureMetadata != null) && (this.availability != null))
        {
            @NotNull final Stage dialog = new Stage();
            dialog.setTitle("Daftar Dosen");
            try
            {
                dialog.setScene(new Scene(ILectureList.load(new CLectureList(this.schoolMetadata, this.dayMetadata, this.periodMetadata, this.availability, this.lectureMetadata)).load()));
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            dialog.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.showAndWait();
        }
    }

    public void onLessonButtonPressed(ActionEvent actionEvent)
    {
        if((this.schoolMetadata != null) && (this.subjectMetadata != null) && (this.classMetadata != null) && (this.lectureMetadata != null) && (this.classroomMetadata != null) && (this.lessonMetadata != null))
        {
            @NotNull final Stage dialog = new Stage();
            dialog.setTitle("Daftar Pelajaran");
            try
            {
                dialog.setScene(new Scene(ILessonList.load(new CLessonList(this.schoolMetadata, this.subjectMetadata, this.classMetadata, this.lectureMetadata, this.classroomMetadata, this.lessonMetadata)).load()));
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            dialog.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.showAndWait();
        }
    }

    @NotNull public BooleanProperty getGenerateListener()
    {
        return this.generateListener;
    }

    public void setSwarmObserver(@NotNull SwarmObserver swarmObserver)
    {
        this.swarmObserver = swarmObserver;
    }

    public void onMenuCategoryGenerateClick(ActionEvent actionEvent)
    {
        if(this.schoolMetadata != null)
        {
            final Runtime runtime = Runtime.getRuntime();
            runtime.runFinalization();
            runtime.gc();
            runtime.runFinalization();
            runtime.gc();
            Executors.newSingleThreadExecutor().execute(() ->
            {
                try
                {
                    @NotNull final AbstractModel   model    = new MSchool(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
                    @NotNull final DBSchool        school   = MSchool.getFromMetadata(model, this.schoolMetadata);
                    @NotNull final DBProblemLoader dbLoader = new DBProblemLoader(school);
                    dbLoader.loadData();
                    @NotNull final DatasetGenerator dsLoader = new DatasetGenerator(dbLoader);
                    @NotNull final PSO              pso      = new PSO(dsLoader, dbLoader.getParameter(), dbLoader.getConstraint());
                    this.generateListener.setValue(true);
                    pso.initialize();
                    this.swarmObserver.update(pso.getParticles());
                    while(!pso.isConditionSatisfied())
                    {
                        pso.updatePBest();
                        pso.assignGBest();
                        pso.evaluateParticle();
                        pso.updateStoppingCondition();
                    }
                    pso.updatePBest();
                    pso.assignGBest();
                    Data.replaceData(pso.getParticle(0).getData(), pso.getGBest());
                    pso.repair(pso.getParticle(0));
                    pso.compress(pso.getParticle(0));
                    pso.calculate(pso.getParticle(0));
                    @NotNull final Observer webContent = (o, arg) ->
                    {
                        if(arg instanceof String)
                        {
                            @NotNull final WebView web_content = new WebView();
                            web_content.getEngine().loadContent((String) arg, "text/html");
                            web_content.getEngine().setJavaScriptEnabled(true);
                            if(this.content != null)
                            {
                                this.generateListener.setValue(false);
                                Notifications.create()
                                             .title("Info")
                                             .text("Generate jadwal Sukses")
                                             .showInformation();
                                this.content.update(null, new Object[] {web_content, Double.valueOf(pso.getFitness())});
                            }
                        }
                    };
                    Platform.runLater(() -> CMCategory.this.createResultResourceFile(webContent, pso.getParticle(0), dsLoader, dbLoader));
                }
                catch(SQLException | UnsupportedEncodingException e)
                {
                    Notifications.create()
                                 .title("Info")
                                 .text("Error, Silahkan Coba Lagi")
                                 .showError();
                }
            });
        }
    }

    private void createResultResourceFile(@NotNull final Observer callback, @NotNull final Particle particle, @NotNull final DatasetGenerator dsLoader, @NotNull final DBProblemLoader dbLoader)
    {
        @SuppressWarnings("StringBufferReplaceableByString") @NotNull final StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>");
        sb.append("<html lang=\"en\">");
        sb.append("<head>");
        sb.append("<meta charset=\"UTF-8\">");
        sb.append("</head>");
        sb.append("<style>");
        sb.append("table {");
        sb.append("border-collapse: collapse;");
        sb.append("}");
        sb.append("");
        sb.append("table, th, td {");
        sb.append("font-size: 12px;");
        sb.append("cursor: pointer;");
        sb.append("border: 1px solid black;");
        sb.append("}");
        sb.append("");
        sb.append("td._dummy, th._dummy{");
        sb.append("height: 0;");
        sb.append("padding: 0;");
        sb.append("width: 50px;");
        sb.append("}");
        sb.append("");
        sb.append("td._not_available {");
        sb.append("background-color: #BDBDBD;");
        sb.append("}");
        sb.append("");
        sb.append("th, td{");
        sb.append("padding: 15px 5px;");
        sb.append("text-align: center;");
        sb.append("vertical-align: middle;");
        sb.append("width: 50px;");
        sb.append("-webkit-touch-callout: none;");
        sb.append("-webkit-user-select: none;");
        sb.append("-khtml-user-select: none;");
        sb.append("-moz-user-select: none;");
        sb.append("-ms-user-select: none;");
        sb.append("user-select: none;");
        sb.append("}");
        sb.append("");
        sb.append("td.divider, th.divider");
        sb.append("{");
        sb.append("width: 75px;");
        sb.append("}");
        sb.append("");
        sb.append("td .content");
        sb.append("{");
        sb.append("color: inherit;");
        sb.append("}");
        sb.append("");
        sb.append("td .content-time");
        sb.append("{");
        sb.append("font-size: 8px;");
        sb.append("color: inherit;");
        sb.append("}");
        sb.append("");
        sb.append("/* Tooltip text */");
        sb.append(".tooltip .tooltiptext {");
        sb.append("visibility: hidden;");
        sb.append("width: 120px;");
        sb.append("background-color: lightgray;");
        sb.append("color: black;");
        sb.append("margin-top: 20px;");
        sb.append("text-align: center;");
        sb.append("padding: 5px 0;");
        sb.append("border-radius: 6px;");
        sb.append("");
        sb.append("/* Position the tooltip text - see examples below! */");
        sb.append("position: absolute;");
        sb.append("z-index: 1;");
        sb.append("}");
        sb.append("");
        sb.append("#header-wrap {");
        sb.append("background: #eeeeff;");
        sb.append("position: fixed;");
        sb.append("width: 100%;");
        sb.append("height: 50px;");
        sb.append("top: 0;");
        sb.append("z-index: 1;");
        sb.append("}");
        sb.append("#container{");
        sb.append("margin-top: 50px;");
        sb.append("}");
        sb.append("");
        sb.append("/* Position the tooltip text - see examples below! */");
        sb.append("");
        sb.append("/* Show the tooltip text when you mouse over the tooltip container */");
        sb.append(".tooltip:hover .tooltiptext {");
        sb.append("visibility: visible;");
        sb.append("}");
        sb.append("</style>");
        sb.append("<body>");
        sb.append("<div id=\"header-wrap\">");
        sb.append("Tampilkan : &nbsp;&nbsp;&nbsp;");
        sb.append("<select id=\"s_t\">");
        sb.append("<option value=\"t_0\">Pelajaran</option>");
        sb.append("<option value=\"t_1\">Dosen</option>");
        sb.append("<option value=\"t_2\">Kelas</option>");
        sb.append("<option value=\"t_3\">Matakuliah</option>");
        sb.append("<option value=\"t_4\">Constraint1</option>");
        sb.append("<option value=\"t_5\">Constraint2</option>");
        sb.append("<option value=\"t_6\">Constraint3</option>");
        sb.append("<option value=\"t_7\">Constraint4</option>");
        sb.append("<option value=\"t_8\">Constraint5</option>");
        sb.append("<option value=\"t_9\">Constraint6</option>");
        sb.append("<option value=\"t_10\">Constraint7</option>");
        sb.append("<option value=\"t_11\">Constraint8</option>");
        sb.append("</select>");
        sb.append("</div>");
        sb.append("<div id=\"container\">");
        //@NotNull final ExecutorService executor = Executors.newCachedThreadPool();
        sb.append(this._formatLesson(particle.getData(), dsLoader, dbLoader));
        sb.append(this._formatLecture(particle.getData(), dsLoader, dbLoader));
        sb.append(this._formatClass(particle.getData(), dsLoader, dbLoader));
        sb.append(this._formatSubject(particle.getData(), dsLoader, dbLoader));
        sb.append(this._formatTimeoff(particle, dsLoader, dbLoader));
        sb.append(this._formatConflict(particle, dsLoader, dbLoader));
        /*executor.execute(() -> sb.append(this._formatLesson(particle.getData(), dsLoader, dbLoader)));
        executor.execute(() -> sb.append(this._formatLecture(particle.getData(), dsLoader, dbLoader)));
        executor.execute(() -> sb.append(this._formatClass(particle.getData(), dsLoader, dbLoader)));
        executor.execute(() -> sb.append(this._formatSubject(particle.getData(), dsLoader, dbLoader)));
        executor.execute(() -> sb.append(this._formatTimeoff(particle, dsLoader, dbLoader)));
        executor.execute(() -> sb.append(this._formatConflict(particle, dsLoader, dbLoader)));
        executor.shutdown();
        try
        {
            //noinspection StatementWithEmptyBody
            while(!executor.awaitTermination(1, TimeUnit.MINUTES))
            {
            }
        }
        catch(InterruptedException ignored)
        {
        }*/
        sb.append("</div>");
        sb.append("");
        sb.append("<script type=\"text/javascript\">");
        sb.append("var tt=[\"tt_0\",\"tt_1\",\"tt_2\",\"tt_3\",\"tt_4\",\"tt_5\",\"tt_6\",\"tt_7\",\"tt_8\",\"tt_9\",\"tt_10\",\"tt_11\"],filter=document.getElementById(\"s_t\"),changeHandler=function(){for(var a=-1,b=tt.length;++a<b;)null!==document.getElementById(tt[a])&&(document.getElementById(tt[a]).style.display=\"none\");var c=this.value.split(\"_\");null!==document.getElementById(\"tt_\"+c[1])&&(document.getElementById(\"tt_\"+c[1]).style.display=\"table\")};filter.addEventListener?filter.addEventListener(\"change\",changeHandler,!1):filter.attachEvent?filter.attachEvent(\"onchange\",changeHandler):filter.onchange=changeHandler,document.getElementById(\"tt_0\").style.display=\"table\";");
        sb.append("</script>");
        sb.append("");
        sb.append("");
        sb.append("");
        sb.append("</body>");
        sb.append("</html>");

        callback.update(null, sb.toString());
    }

    @NotNull @SuppressWarnings({"ConstantConditions", "Duplicates"}) private String _formatConflict(@NotNull final Particle particle, @NotNull final DatasetGenerator dsLoader, @NotNull final DBProblemLoader dbLoader)
    {
        /*
        * Initialize Conflict placement property
        * */
        @NotNull final PlacementProperty placement_property = particle.getPlacementProperty();
        final DSTimeOffPlacement[]       lecture_placement  = placement_property.getLecturePlacement();
        final DSTimeOffPlacement[]       class_placement    = placement_property.getClassPlacement();
        final IntHList                   lecture_fill       = placement_property.getLectureFill();
        final IntHList                   class_fill         = placement_property.getClassFill();

        /*
        * Initialize fitness value
        * */
        double[] fitness = {0.0, 0.0, 0.0, 0.0};

        /*
        * Initialize Container
        * */
        @NotNull final Int2ObjectMap<Int2ObjectMap<StringBuilder>> query_subject = new Int2ObjectLinkedOpenHashMap<>(dbLoader.getClassroomSize());
        @NotNull final Int2ObjectMap<Int2ObjectMap<StringBuilder>> query_lecture = new Int2ObjectLinkedOpenHashMap<>(dbLoader.getClassroomSize());
        @NotNull final Int2ObjectMap<Int2ObjectMap<StringBuilder>> query_class   = new Int2ObjectLinkedOpenHashMap<>(dbLoader.getClassroomSize());
        @NotNull final Int2ObjectMap<Int2ObjectMap<StringBuilder>> query_period  = new Int2ObjectLinkedOpenHashMap<>(dbLoader.getClassroomSize());

        @NotNull final DBSchool  empty_school  = new DBSchool(-1, "", "", "", "", -1, 1, 1);
        @NotNull final DBSubject empty_subject = new DBSubject(-1, "Tidak Ada", "", empty_school);
        @NotNull final DBLecture empty_lecture = new DBLecture(-1, "Tidak Ada", empty_school);
        @NotNull final DBClass   empty_class   = new DBClass(-1, "Tidak Ada", empty_school);

        @NotNull final DSLesson[]                 encoded_lessons       = dsLoader.getDataset().getLessons();
        @NotNull final Int2IntMap                 classroom_lv0_decoder = dsLoader.getDecoder().getClassrooms();
        @NotNull final Int2IntMap                 day_decoder           = dsLoader.getDecoder().getActiveDays();
        @NotNull final Int2IntMap                 period_decoder        = dsLoader.getDecoder().getActivePeriods();
        @NotNull final Int2IntMap                 subject_decoder       = dsLoader.getDecoder().getSubjects();
        @NotNull final Int2IntMap                 lecture_decoder       = dsLoader.getDecoder().getLecturers();
        @NotNull final Int2IntMap                 class_decoder         = dsLoader.getDecoder().getClasses();
        @NotNull final Int2ObjectMap<DBClassroom> decoded_classrooms    = dbLoader.getClassrooms();
        @NotNull final Int2ObjectMap<DBDay>       decoded_days          = dbLoader.getDays();
        @NotNull final Int2ObjectMap<DBSubject>   decoded_subjects      = dbLoader.getSubjects();
        @NotNull final Int2ObjectMap<DBLecture>   decoded_lecturers     = dbLoader.getLecturers();
        @NotNull final Int2ObjectMap<DBClass>     decoded_classes       = dbLoader.getClasses();
        @NotNull final Int2ObjectMap<DBPeriod>    decoded_period        = dbLoader.getPeriods();

        final ColorPair[] timeOffColor = new ColorPair[3];
        timeOffColor[0] = new ColorPair(new Color(0xE5, 0x73, 0x73));
        timeOffColor[1] = new ColorPair(new Color(0x81, 0xC7, 0x84));

        int i_cluster = -1;
        for(@NotNull final DSLessonCluster lesson_cluster : dsLoader.getDataset().getLessonClusters())
        {
            /*
            * Increment day index
            * */
            ++i_cluster;
            @NotNull final Int2IntMap classroom_lv1_decoder = lesson_cluster.getClassroomDecoder();

            /*
            * Get lessons data in current cluster
            * */
            final int[] lessons = particle.getData().getPosition(i_cluster).getPosition();

            /*
            * Initialize lesson counter
            * */
            int c_lesson = -1;

            /*
            * Get lesson according to lesson counter
            * */
            DSLesson lesson = encoded_lessons[lessons[++c_lesson]];

            /*
            * Get current lesson sks
            * */
            int lesson_sks = lesson == null ? 1 : lesson.getSks();

            /*
            * Initialize sks counter
            * */
            int c_sks = 0;

            /*
            * Foreach classroom in current cluster
            * */
            for(final int classroom : lesson_cluster.getClassrooms())
            {
                final int    decoded_classroom_id = classroom_lv0_decoder.get(classroom_lv1_decoder.get(classroom));
                final String classroom_name       = decoded_classrooms.get(decoded_classroom_id).getName();
                if(!query_subject.containsKey(decoded_classroom_id))
                {
                    query_subject.put(decoded_classroom_id, new Int2ObjectLinkedOpenHashMap<>(decoded_days.size()));
                    query_lecture.put(decoded_classroom_id, new Int2ObjectLinkedOpenHashMap<>(decoded_days.size()));
                    query_class.put(decoded_classroom_id, new Int2ObjectLinkedOpenHashMap<>(decoded_days.size()));
                    query_period.put(decoded_classroom_id, new Int2ObjectLinkedOpenHashMap<>(decoded_days.size()));
                }

                /*
                * Initialize day index
                * Foreach day in current classroom
                * */
                int i_day = -1;
                for(final double[] day : lesson_cluster.getClassroomsTimeoff(classroom).getTimeoff())
                {
                    /*
                    * Increment day index
                    * */
                    ++i_day;

                    final int    decoded_day_id = day_decoder.get(i_day);
                    final String day_name       = decoded_days.get(decoded_day_id).getName();

                    if(!query_subject.get(decoded_classroom_id).containsKey(decoded_day_id))
                    {
                        query_subject.get(decoded_classroom_id).put(decoded_day_id, new StringBuilder());
                        query_lecture.get(decoded_classroom_id).put(decoded_day_id, new StringBuilder());
                        query_class.get(decoded_classroom_id).put(decoded_day_id, new StringBuilder());
                        query_period.get(decoded_classroom_id).put(decoded_day_id, new StringBuilder());
                    }
                    @SuppressWarnings("MismatchedQueryAndUpdateOfStringBuilder")
                    @NotNull final StringBuilder[] tmp_query = new StringBuilder[4];
                    tmp_query[0] = query_subject.get(decoded_classroom_id).get(decoded_day_id);
                    tmp_query[1] = query_lecture.get(decoded_classroom_id).get(decoded_day_id);
                    tmp_query[2] = query_class.get(decoded_classroom_id).get(decoded_day_id);
                    tmp_query[3] = query_period.get(decoded_classroom_id).get(decoded_day_id);

                    /*
                    * Initialize period index
                    * Foreach all period within day
                    * */
                    int i_period = -1;
                    for(final double period : day)
                    {
                       /*
                        * Increment period index
                        * */
                        ++i_period;

                        /*
                        * If current period time off is available
                        * */
                        if(period != 0.2)
                        {
                            /*
                            * Get lecture and klass information from current lesson
                            * */
                            if(lesson == null)
                            {
                                lecture_fill.add(-1);
                                class_fill.add(-1);
                            }
                            else
                            {
                                final int lecture = lesson.getLecture();
                                final int klass   = lesson.getKlass();

                                fitness[0] += (lecture == -1 ? 10 : (lecture_placement[lecture].putPlacementIfAbsent(i_day, i_period, lessons[c_lesson]) ? 10 : 0.1));
                                fitness[1] += (class_placement[klass].putPlacementIfAbsent(i_day, i_period, lessons[c_lesson]) ? 10 : 0.1);
                                fitness[2] += (lesson.getLinkTotal() == 0 ? 10 : (class_placement[klass].isNotTheSameDay(i_day, lesson.getLessonLink()) ? 10 : 0.1));
                                fitness[3] += (lesson.isLessonAllowed(classroom) ? 10 : 0.1);

                                lecture_fill.add(lecture);
                                class_fill.add(klass);
                            }

                            /*
                            * check if counter sks have same value with lesson sks
                            * */
                            if(++c_sks == lesson_sks)
                            {
                                for(int c_tmp = -1, c_tmps = tmp_query.length; ++c_tmp < c_tmps; )
                                {
                                    tmp_query[c_tmp].append("<td class=\"tooltip\" colspan=\"")
                                                    .append(lesson_sks)
                                                    .append("\" style=\"color: ")
                                                    .append(lesson != null ?
                                                            (fitness[c_tmp] >= (lesson_sks * 10) ?
                                                                    ColorPair.parseColor(timeOffColor[1].foreground) :
                                                                    (fitness[c_tmp] >= (lesson_sks * 0.1) ?
                                                                            ColorPair.parseColor(timeOffColor[0].foreground) :
                                                                            "#000000"
                                                                    )
                                                            ) :
                                                            "#000000")
                                                    .append("; background-color: ")
                                                    .append(lesson != null ?
                                                            (fitness[c_tmp] >= (lesson_sks * 10) ?
                                                                    ColorPair.parseColor(timeOffColor[1].background) :
                                                                    (fitness[c_tmp] >= (lesson_sks * 0.1) ?
                                                                            ColorPair.parseColor(timeOffColor[0].background) :
                                                                            "#FFFFFF"
                                                                    )
                                                            ) :
                                                            "#FFFFFF")
                                                    .append("\">");
                                }
                                if(lesson != null)
                                {
                                    @NotNull final String[]      splitted_class = decoded_classes.getOrDefault(class_decoder.get(lesson.getKlass()), empty_class).getName().split("-");
                                    @NotNull final StringBuilder _tmp_query     = new StringBuilder();
                                    _tmp_query.append("<strong class=\"content\">")
                                              .append(decoded_subjects.getOrDefault(subject_decoder.get(lesson.getSubject()), empty_subject).getName())
                                              .append("</strong>");
                                    _tmp_query.append("<br>");
                                    _tmp_query.append("<span class=\"content\">")
                                              .append(decoded_lecturers.getOrDefault(lecture_decoder.get(lesson.getLecture()), empty_lecture).getName())
                                              .append("</span>");
                                    _tmp_query.append("<br>");
                                    _tmp_query.append("<span class=\"content\">")
                                              .append("Kelas - ")
                                              .append(splitted_class[splitted_class.length - 1])
                                              .append("</span>");
                                    _tmp_query.append("<br>");
                                    _tmp_query.append("<strong class=\"content-time\">")
                                              .append(decoded_period.get(period_decoder.get(i_period - (lesson_sks - 1))).getStart())
                                              .append(" - ")
                                              .append(decoded_period.get(period_decoder.get(i_period)).getEnd())
                                              .append("</strong>");
                                    _tmp_query.append("<div class=\"tooltiptext\">")
                                              .append(day_name)
                                              .append(" - ")
                                              .append(classroom_name)
                                              .append("</div>");
                                    for(int c_tmp = -1, c_tmps = tmp_query.length; ++c_tmp < c_tmps; )
                                    {
                                        tmp_query[c_tmp].append(_tmp_query);
                                    }
                                }
                                for(int c_tmp = -1, c_tmps = tmp_query.length; ++c_tmp < c_tmps; )
                                {
                                    tmp_query[c_tmp].append("</td>");
                                }

                                /*
                                * Refill current lesson data
                                * */
                                try
                                {
                                    lesson = encoded_lessons[lessons[++c_lesson]];
                                }
                                catch(ArrayIndexOutOfBoundsException ignored)
                                {
                                }
                                finally
                                {
                                    lesson_sks = lesson == null ? 1 : lesson.getSks();
                                    c_sks = 0;
                                    fitness[0] = fitness[1] = fitness[2] = fitness[3] = 0;
                                }
                            }
                        }
                        else
                        {
                            for(int c_tmp = -1, c_tmps = tmp_query.length; ++c_tmp < c_tmps; )
                            {
                                tmp_query[c_tmp].append("<td class=\"tooltip _not_available\" colspan=\"").append(1).append("\" style=\"color: black\">");
                                tmp_query[c_tmp].append("</td>");
                            }
                            lecture_fill.add(-1);
                            class_fill.add(-1);
                        }
                    }
                }
            }
        }

        /*
        * generated arranged day
        * */
        @NotNull final int[] arranged_day = new int[decoded_days.size()];
        for(@NotNull final DBDay _day : dbLoader.getDays().values())
        {
            arranged_day[_day.getPosition() - 1] = _day.getId();
        }

        //noinspection Convert2streamapi
        for(final int _classroom : dbLoader.getClassrooms().keySet())
        {
            if(!query_subject.containsKey(_classroom))
            {
                query_subject.put(_classroom, new Int2ObjectLinkedOpenHashMap<>(arranged_day.length));
                query_lecture.put(_classroom, new Int2ObjectLinkedOpenHashMap<>(arranged_day.length));
                query_class.put(_classroom, new Int2ObjectLinkedOpenHashMap<>(arranged_day.length));
                query_period.put(_classroom, new Int2ObjectLinkedOpenHashMap<>(arranged_day.length));
                for(final int _day : arranged_day)
                {
                    @NotNull final StringBuilder tmp_query = new StringBuilder();
                    //noinspection unchecked
                    for(final DBTimeOff timeoff : (ObjectList<DBTimeOff>) dbLoader.getClassroom(_classroom).getTimeoff().getAvailabilities().get(_day - 1))
                    {
                        if(timeoff.getAvailability().getId() == 1)
                        {
                            tmp_query.append("<td class=\"tooltip _not_available\" colspan=\"").append(1).append("\" style=\"color: black\">");
                            tmp_query.append("</td>");
                        }
                        else
                        {
                            tmp_query.append("<td class=\"tooltip\" colspan=\"").append(1).append("\" style=\"color: black\">");
                            tmp_query.append("</td>");
                        }
                    }
                    query_subject.get(_classroom).put(_day, tmp_query);
                    query_lecture.get(_classroom).put(_day, tmp_query);
                    query_class.get(_classroom).put(_day, tmp_query);
                    query_period.get(_classroom).put(_day, tmp_query);
                }
            }
        }

        @NotNull final StringBuilder combined_subject = new StringBuilder();
        @NotNull final StringBuilder combined_lecture = new StringBuilder();
        @NotNull final StringBuilder combined_class   = new StringBuilder();
        @NotNull final StringBuilder combined_period  = new StringBuilder();
        combined_subject.append("<table id=\"tt_8\" width=\"")
                        .append(((decoded_period.size() * decoded_days.size() + 1) * 60) + ((decoded_days.size() - 1) * 85))
                        .append("px\" style=\"display:none\">");
        combined_subject.append("<tr>");
        combined_lecture.append("<table id=\"tt_9\" width=\"")
                        .append(((decoded_period.size() * decoded_days.size() + 1) * 60) + ((decoded_days.size() - 1) * 85))
                        .append("px\" style=\"display:none\">");
        combined_lecture.append("<tr>");
        combined_class.append("<table id=\"tt_10\" width=\"")
                      .append(((decoded_period.size() * decoded_days.size() + 1) * 60) + ((decoded_days.size() - 1) * 85))
                      .append("px\" style=\"display:none\">");
        combined_class.append("<tr>");
        combined_period.append("<table id=\"tt_11\" width=\"")
                       .append(((decoded_period.size() * decoded_days.size() + 1) * 60) + ((decoded_days.size() - 1) * 85))
                       .append("px\" style=\"display:none\">");
        combined_period.append("<tr>");
        for(int c_d = -1, c_ds = arranged_day.length; ++c_d < c_ds; )
        {
            combined_subject.append("<th class=\"_dummy\"></th>");
            combined_lecture.append("<th class=\"_dummy\"></th>");
            combined_class.append("<th class=\"_dummy\"></th>");
            combined_period.append("<th class=\"_dummy\"></th>");
            for(int c_p = -1, c_ps = decoded_period.size(); ++c_p < c_ps; )
            {
                combined_subject.append("<th class=\"_dummy\"></th>");
                combined_lecture.append("<th class=\"_dummy\"></th>");
                combined_class.append("<th class=\"_dummy\"></th>");
                combined_period.append("<th class=\"_dummy\"></th>");
            }
        }
        combined_subject.append("</tr>");
        combined_subject.append("<tr>");
        combined_subject.append("<th colspan=\"1\">Hari --> <br>Ruangan</th>");
        combined_lecture.append("</tr>");
        combined_lecture.append("<tr>");
        combined_lecture.append("<th colspan=\"1\">Hari --> <br>Ruangan</th>");
        combined_class.append("</tr>");
        combined_class.append("<tr>");
        combined_class.append("<th colspan=\"1\">Hari --> <br>Ruangan</th>");
        combined_period.append("</tr>");
        combined_period.append("<tr>");
        combined_period.append("<th colspan=\"1\">Hari --> <br>Ruangan</th>");
        for(int c_d = -1, c_ds = arranged_day.length, c_ps = decoded_period.size(), c_cs = decoded_classrooms.size(); ++c_d < c_ds; )
        {
            combined_subject.append("<th colspan=\"")
                            .append(c_ps)
                            .append("\">")
                            .append(decoded_days.get(arranged_day[c_d]).getName())
                            .append("</th>");
            combined_lecture.append("<th colspan=\"")
                            .append(c_ps)
                            .append("\">")
                            .append(decoded_days.get(arranged_day[c_d]).getName())
                            .append("</th>");
            combined_class.append("<th colspan=\"")
                          .append(c_ps)
                          .append("\">")
                          .append(decoded_days.get(arranged_day[c_d]).getName())
                          .append("</th>");
            combined_period.append("<th colspan=\"")
                           .append(c_ps)
                           .append("\">")
                           .append(decoded_days.get(arranged_day[c_d]).getName())
                           .append("</th>");
            if((c_d + 1) != c_ds)
            {
                combined_subject.append("<th class=\"divider\" rowspan=\"")
                                .append(c_cs + 1)
                                .append("\"></th>");
                combined_lecture.append("<th class=\"divider\" rowspan=\"")
                                .append(c_cs + 1)
                                .append("\"></th>");
                combined_class.append("<th class=\"divider\" rowspan=\"")
                              .append(c_cs + 1)
                              .append("\"></th>");
                combined_period.append("<th class=\"divider\" rowspan=\"")
                               .append(c_cs + 1)
                               .append("\"></th>");
            }
        }
        combined_subject.append("</tr>");
        combined_lecture.append("</tr>");
        combined_class.append("</tr>");
        combined_period.append("</tr>");
        for(final int _classroom : decoded_classrooms.keySet())
        {
            combined_subject.append("<tr>");
            combined_subject.append("<th>")
                            .append(decoded_classrooms.get(_classroom).getName())
                            .append("</th>");
            combined_lecture.append("<tr>");
            combined_lecture.append("<th>")
                            .append(decoded_classrooms.get(_classroom).getName())
                            .append("</th>");
            combined_class.append("<tr>");
            combined_class.append("<th>")
                          .append(decoded_classrooms.get(_classroom).getName())
                          .append("</th>");
            combined_period.append("<tr>");
            combined_period.append("<th>")
                           .append(decoded_classrooms.get(_classroom).getName())
                           .append("</th>");
            for(final int _day : arranged_day)
            {
                combined_subject.append(query_subject.get(_classroom).get(_day));
                combined_lecture.append(query_lecture.get(_classroom).get(_day));
                combined_class.append(query_class.get(_classroom).get(_day));
                combined_period.append(query_period.get(_classroom).get(_day));
            }
            combined_subject.append("</tr>");
            combined_lecture.append("</tr>");
            combined_class.append("</tr>");
            combined_period.append("</tr>");
        }
        combined_subject.append("</table>");
        combined_lecture.append("</table>");
        combined_class.append("</table>");
        combined_period.append("</table>");
        return combined_subject.append(combined_lecture).append(combined_class).append(combined_period).toString();
    }

    @NotNull @SuppressWarnings({"ConstantConditions", "Duplicates"}) private String _formatTimeoff(@NotNull final Particle particle, @NotNull final DatasetGenerator dsLoader, @NotNull final DBProblemLoader dbLoader)
    {
        /*
        * Initialize fitness value
        * */
        double[] fitness = {0.0, 0.0, 0.0, 0.0};

        /*
        * Initialize Container
        * */
        @NotNull final Int2ObjectMap<Int2ObjectMap<StringBuilder>> query_subject = new Int2ObjectLinkedOpenHashMap<>(dbLoader.getClassroomSize());
        @NotNull final Int2ObjectMap<Int2ObjectMap<StringBuilder>> query_lecture = new Int2ObjectLinkedOpenHashMap<>(dbLoader.getClassroomSize());
        @NotNull final Int2ObjectMap<Int2ObjectMap<StringBuilder>> query_class   = new Int2ObjectLinkedOpenHashMap<>(dbLoader.getClassroomSize());
        @NotNull final Int2ObjectMap<Int2ObjectMap<StringBuilder>> query_period  = new Int2ObjectLinkedOpenHashMap<>(dbLoader.getClassroomSize());

        @NotNull final DBSchool  empty_school  = new DBSchool(-1, "", "", "", "", -1, 1, 1);
        @NotNull final DBSubject empty_subject = new DBSubject(-1, "Tidak Ada", "", empty_school);
        @NotNull final DBLecture empty_lecture = new DBLecture(-1, "Tidak Ada", empty_school);
        @NotNull final DBClass   empty_class   = new DBClass(-1, "Tidak Ada", empty_school);

        @NotNull final DSLesson[]                 encoded_lessons       = dsLoader.getDataset().getLessons();
        @NotNull final DSTimeOff[]                timeoff_subject       = dsLoader.getDataset().getSubjects();
        @NotNull final DSTimeOff[]                timeoff_lecture       = dsLoader.getDataset().getLecturers();
        @NotNull final DSTimeOff[]                timeoff_class         = dsLoader.getDataset().getClasses();
        @NotNull final Int2IntMap                 classroom_lv0_decoder = dsLoader.getDecoder().getClassrooms();
        @NotNull final Int2IntMap                 day_decoder           = dsLoader.getDecoder().getActiveDays();
        @NotNull final Int2IntMap                 period_decoder        = dsLoader.getDecoder().getActivePeriods();
        @NotNull final Int2IntMap                 subject_decoder       = dsLoader.getDecoder().getSubjects();
        @NotNull final Int2IntMap                 lecture_decoder       = dsLoader.getDecoder().getLecturers();
        @NotNull final Int2IntMap                 class_decoder         = dsLoader.getDecoder().getClasses();
        @NotNull final Int2ObjectMap<DBClassroom> decoded_classrooms    = dbLoader.getClassrooms();
        @NotNull final Int2ObjectMap<DBDay>       decoded_days          = dbLoader.getDays();
        @NotNull final Int2ObjectMap<DBSubject>   decoded_subjects      = dbLoader.getSubjects();
        @NotNull final Int2ObjectMap<DBLecture>   decoded_lecturers     = dbLoader.getLecturers();
        @NotNull final Int2ObjectMap<DBClass>     decoded_classes       = dbLoader.getClasses();
        @NotNull final Int2ObjectMap<DBPeriod>    decoded_period        = dbLoader.getPeriods();

        final ColorPair[] timeOffColor = new ColorPair[3];
        timeOffColor[0] = new ColorPair(new Color(0xE5, 0x73, 0x73));
        timeOffColor[1] = new ColorPair(new Color(0x81, 0xC7, 0x84));
        timeOffColor[2] = new ColorPair(new Color(0xFF, 0xF1, 0x76));

        int i_cluster = -1;
        for(@NotNull final DSLessonCluster lesson_cluster : dsLoader.getDataset().getLessonClusters())
        {
            /*
            * Increment day index
            * */
            ++i_cluster;
            @NotNull final Int2IntMap classroom_lv1_decoder = lesson_cluster.getClassroomDecoder();

            /*
            * Get lessons data in current cluster
            * */
            final int[] lessons = particle.getData().getPosition(i_cluster).getPosition();

            /*
            * Initialize lesson counter
            * */
            int c_lesson = -1;

            /*
            * Get lesson according to lesson counter
            * */
            DSLesson lesson = encoded_lessons[lessons[++c_lesson]];

            /*
            * Get current lesson sks
            * */
            int lesson_sks = lesson == null ? 1 : lesson.getSks();

            /*
            * Initialize sks counter
            * */
            int c_sks = 0;

            /*
            * Foreach classroom in current cluster
            * */
            for(final int classroom : lesson_cluster.getClassrooms())
            {
                final int    decoded_classroom_id = classroom_lv0_decoder.get(classroom_lv1_decoder.get(classroom));
                final String classroom_name       = decoded_classrooms.get(decoded_classroom_id).getName();
                if(!query_subject.containsKey(decoded_classroom_id))
                {
                    query_subject.put(decoded_classroom_id, new Int2ObjectLinkedOpenHashMap<>(decoded_days.size()));
                    query_lecture.put(decoded_classroom_id, new Int2ObjectLinkedOpenHashMap<>(decoded_days.size()));
                    query_class.put(decoded_classroom_id, new Int2ObjectLinkedOpenHashMap<>(decoded_days.size()));
                    query_period.put(decoded_classroom_id, new Int2ObjectLinkedOpenHashMap<>(decoded_days.size()));
                }

                /*
                * Initialize day index
                * Foreach day in current classroom
                * */
                int i_day = -1;
                for(final double[] day : lesson_cluster.getClassroomsTimeoff(classroom).getTimeoff())
                {
                    /*
                    * Increment day index
                    * */
                    ++i_day;

                    final int    decoded_day_id = day_decoder.get(i_day);
                    final String day_name       = decoded_days.get(decoded_day_id).getName();

                    if(!query_subject.get(decoded_classroom_id).containsKey(decoded_day_id))
                    {
                        query_subject.get(decoded_classroom_id).put(decoded_day_id, new StringBuilder());
                        query_lecture.get(decoded_classroom_id).put(decoded_day_id, new StringBuilder());
                        query_class.get(decoded_classroom_id).put(decoded_day_id, new StringBuilder());
                        query_period.get(decoded_classroom_id).put(decoded_day_id, new StringBuilder());
                    }
                    @SuppressWarnings("MismatchedQueryAndUpdateOfStringBuilder")
                    @NotNull final StringBuilder[] tmp_query = new StringBuilder[4];
                    tmp_query[0] = query_subject.get(decoded_classroom_id).get(decoded_day_id);
                    tmp_query[1] = query_lecture.get(decoded_classroom_id).get(decoded_day_id);
                    tmp_query[2] = query_class.get(decoded_classroom_id).get(decoded_day_id);
                    tmp_query[3] = query_period.get(decoded_classroom_id).get(decoded_day_id);

                    /*
                    * Initialize period index
                    * Foreach all period within day
                    * */
                    int i_period = -1;
                    for(final double period : day)
                    {
                       /*
                        * Increment period index
                        * */
                        ++i_period;

                        /*
                        * If current period time off is available
                        * */
                        if(period != 0.2)
                        {
                            /*
                            * Get lecture and klass information from current lesson
                            * */
                            if(lesson != null)
                            {
                                final int lecture = lesson.getLecture();
                                final int klass   = lesson.getKlass();

                                fitness[0] += (timeoff_subject[lesson.getSubject()].get(i_day, i_period));
                                fitness[1] += (lecture == -1 ? 10 : (timeoff_lecture[lecture].get(i_day, i_period)));
                                fitness[2] += (timeoff_class[klass].get(i_day, i_period));
                                fitness[3] += (period);
                            }

                            /*
                            * check if counter sks have same value with lesson sks
                            * */
                            if(++c_sks == lesson_sks)
                            {
                                for(int c_tmp = -1, c_tmps = tmp_query.length; ++c_tmp < c_tmps; )
                                {
                                    tmp_query[c_tmp].append("<td class=\"tooltip\" colspan=\"")
                                                    .append(lesson_sks)
                                                    .append("\" style=\"color: ")
                                                    .append(lesson != null ?
                                                            (fitness[c_tmp] >= (lesson_sks * 10) ?
                                                                    ColorPair.parseColor(timeOffColor[1].foreground) :
                                                                    (fitness[c_tmp] >= (lesson_sks * 10) ?
                                                                            ColorPair.parseColor(timeOffColor[2].foreground) :
                                                                            (fitness[c_tmp] >= (lesson_sks * 0.2) ?
                                                                                    ColorPair.parseColor(timeOffColor[0].foreground) :
                                                                                    "#000000"
                                                                            )
                                                                    )
                                                            ) :
                                                            "#000000")
                                                    .append("; background-color: ")
                                                    .append(lesson != null ?
                                                            (fitness[c_tmp] >= (lesson_sks * 10) ?
                                                                    ColorPair.parseColor(timeOffColor[1].background) :
                                                                    (fitness[c_tmp] >= (lesson_sks) ?
                                                                            ColorPair.parseColor(timeOffColor[2].background) :
                                                                            (fitness[c_tmp] >= (lesson_sks * 0.2) ?
                                                                                    ColorPair.parseColor(timeOffColor[0].background) :
                                                                                    "#FFFFFF"
                                                                            )
                                                                    )
                                                            ) :
                                                            "#FFFFFF")
                                                    .append("\">");
                                }
                                if(lesson != null)
                                {
                                    @NotNull final String[]      splitted_class = decoded_classes.getOrDefault(class_decoder.get(lesson.getKlass()), empty_class).getName().split("-");
                                    @NotNull final StringBuilder _tmp_query     = new StringBuilder();
                                    _tmp_query.append("<strong class=\"content\">")
                                              .append(decoded_subjects.getOrDefault(subject_decoder.get(lesson.getSubject()), empty_subject).getName())
                                              .append("</strong>");
                                    _tmp_query.append("<br>");
                                    _tmp_query.append("<span class=\"content\">")
                                              .append(decoded_lecturers.getOrDefault(lecture_decoder.get(lesson.getLecture()), empty_lecture).getName())
                                              .append("</span>");
                                    _tmp_query.append("<br>");
                                    _tmp_query.append("<span class=\"content\">")
                                              .append("Kelas - ")
                                              .append(splitted_class[splitted_class.length - 1])
                                              .append("</span>");
                                    _tmp_query.append("<br>");
                                    _tmp_query.append("<strong class=\"content-time\">")
                                              .append(decoded_period.get(period_decoder.get(i_period - (lesson_sks - 1))).getStart())
                                              .append(" - ")
                                              .append(decoded_period.get(period_decoder.get(i_period)).getEnd())
                                              .append("</strong>");
                                    _tmp_query.append("<div class=\"tooltiptext\">")
                                              .append(day_name)
                                              .append(" - ")
                                              .append(classroom_name)
                                              .append("</div>");
                                    for(int c_tmp = -1, c_tmps = tmp_query.length; ++c_tmp < c_tmps; )
                                    {
                                        tmp_query[c_tmp].append(_tmp_query);
                                    }
                                }
                                for(int c_tmp = -1, c_tmps = tmp_query.length; ++c_tmp < c_tmps; )
                                {
                                    tmp_query[c_tmp].append("</td>");
                                }

                                /*
                                * Refill current lesson data
                                * */
                                try
                                {
                                    lesson = encoded_lessons[lessons[++c_lesson]];
                                }
                                catch(ArrayIndexOutOfBoundsException ignored)
                                {
                                }
                                finally
                                {
                                    lesson_sks = lesson == null ? 1 : lesson.getSks();
                                    c_sks = 0;
                                    fitness[0] = fitness[1] = fitness[2] = fitness[3] = 0;
                                }
                            }
                        }
                        else
                        {
                            for(int c_tmp = -1, c_tmps = tmp_query.length; ++c_tmp < c_tmps; )
                            {
                                tmp_query[c_tmp].append("<td class=\"tooltip _not_available\" colspan=\"").append(1).append("\" style=\"color: black\">");
                                tmp_query[c_tmp].append("</td>");
                            }
                        }
                    }
                }
            }
        }

        /*
        * generated arranged day
        * */
        @NotNull final int[] arranged_day = new int[decoded_days.size()];
        for(@NotNull final DBDay _day : dbLoader.getDays().values())
        {
            arranged_day[_day.getPosition() - 1] = _day.getId();
        }

        //noinspection Convert2streamapi
        for(final int _classroom : dbLoader.getClassrooms().keySet())
        {
            if(!query_subject.containsKey(_classroom))
            {
                query_subject.put(_classroom, new Int2ObjectLinkedOpenHashMap<>(arranged_day.length));
                query_lecture.put(_classroom, new Int2ObjectLinkedOpenHashMap<>(arranged_day.length));
                query_class.put(_classroom, new Int2ObjectLinkedOpenHashMap<>(arranged_day.length));
                query_period.put(_classroom, new Int2ObjectLinkedOpenHashMap<>(arranged_day.length));
                for(final int _day : arranged_day)
                {
                    @NotNull final StringBuilder tmp_query = new StringBuilder();
                    //noinspection unchecked
                    for(final DBTimeOff timeoff : (ObjectList<DBTimeOff>) dbLoader.getClassroom(_classroom).getTimeoff().getAvailabilities().get(_day - 1))
                    {
                        if(timeoff.getAvailability().getId() == 1)
                        {
                            tmp_query.append("<td class=\"tooltip _not_available\" colspan=\"").append(1).append("\" style=\"color: black\">");
                            tmp_query.append("</td>");
                        }
                        else
                        {
                            tmp_query.append("<td class=\"tooltip\" colspan=\"").append(1).append("\" style=\"color: black\">");
                            tmp_query.append("</td>");
                        }
                    }
                    query_subject.get(_classroom).put(_day, tmp_query);
                    query_lecture.get(_classroom).put(_day, tmp_query);
                    query_class.get(_classroom).put(_day, tmp_query);
                    query_period.get(_classroom).put(_day, tmp_query);
                }
            }
        }

        @NotNull final StringBuilder combined_subject = new StringBuilder();
        @NotNull final StringBuilder combined_lecture = new StringBuilder();
        @NotNull final StringBuilder combined_class   = new StringBuilder();
        @NotNull final StringBuilder combined_period  = new StringBuilder();
        combined_subject.append("<table id=\"tt_4\" width=\"")
                        .append(((decoded_period.size() * decoded_days.size() + 1) * 60) + ((decoded_days.size() - 1) * 85))
                        .append("px\" style=\"display:none\">");
        combined_subject.append("<tr>");
        combined_lecture.append("<table id=\"tt_5\" width=\"")
                        .append(((decoded_period.size() * decoded_days.size() + 1) * 60) + ((decoded_days.size() - 1) * 85))
                        .append("px\" style=\"display:none\">");
        combined_lecture.append("<tr>");
        combined_class.append("<table id=\"tt_6\" width=\"")
                      .append(((decoded_period.size() * decoded_days.size() + 1) * 60) + ((decoded_days.size() - 1) * 85))
                      .append("px\" style=\"display:none\">");
        combined_class.append("<tr>");
        combined_period.append("<table id=\"tt_7\" width=\"")
                       .append(((decoded_period.size() * decoded_days.size() + 1) * 60) + ((decoded_days.size() - 1) * 85))
                       .append("px\" style=\"display:none\">");
        combined_period.append("<tr>");
        for(int c_d = -1, c_ds = arranged_day.length; ++c_d < c_ds; )
        {
            combined_subject.append("<th class=\"_dummy\"></th>");
            combined_lecture.append("<th class=\"_dummy\"></th>");
            combined_class.append("<th class=\"_dummy\"></th>");
            combined_period.append("<th class=\"_dummy\"></th>");
            for(int c_p = -1, c_ps = decoded_period.size(); ++c_p < c_ps; )
            {
                combined_subject.append("<th class=\"_dummy\"></th>");
                combined_lecture.append("<th class=\"_dummy\"></th>");
                combined_class.append("<th class=\"_dummy\"></th>");
                combined_period.append("<th class=\"_dummy\"></th>");
            }
        }
        combined_subject.append("</tr>");
        combined_subject.append("<tr>");
        combined_subject.append("<th colspan=\"1\">Hari --> <br>Ruangan</th>");
        combined_lecture.append("</tr>");
        combined_lecture.append("<tr>");
        combined_lecture.append("<th colspan=\"1\">Hari --> <br>Ruangan</th>");
        combined_class.append("</tr>");
        combined_class.append("<tr>");
        combined_class.append("<th colspan=\"1\">Hari --> <br>Ruangan</th>");
        combined_period.append("</tr>");
        combined_period.append("<tr>");
        combined_period.append("<th colspan=\"1\">Hari --> <br>Ruangan</th>");
        for(int c_d = -1, c_ds = arranged_day.length, c_ps = decoded_period.size(), c_cs = decoded_classrooms.size(); ++c_d < c_ds; )
        {
            combined_subject.append("<th colspan=\"")
                            .append(c_ps)
                            .append("\">")
                            .append(decoded_days.get(arranged_day[c_d]).getName())
                            .append("</th>");
            combined_lecture.append("<th colspan=\"")
                            .append(c_ps)
                            .append("\">")
                            .append(decoded_days.get(arranged_day[c_d]).getName())
                            .append("</th>");
            combined_class.append("<th colspan=\"")
                          .append(c_ps)
                          .append("\">")
                          .append(decoded_days.get(arranged_day[c_d]).getName())
                          .append("</th>");
            combined_period.append("<th colspan=\"")
                           .append(c_ps)
                           .append("\">")
                           .append(decoded_days.get(arranged_day[c_d]).getName())
                           .append("</th>");
            if((c_d + 1) != c_ds)
            {
                combined_subject.append("<th class=\"divider\" rowspan=\"")
                                .append(c_cs + 1)
                                .append("\"></th>");
                combined_lecture.append("<th class=\"divider\" rowspan=\"")
                                .append(c_cs + 1)
                                .append("\"></th>");
                combined_class.append("<th class=\"divider\" rowspan=\"")
                              .append(c_cs + 1)
                              .append("\"></th>");
                combined_period.append("<th class=\"divider\" rowspan=\"")
                               .append(c_cs + 1)
                               .append("\"></th>");
            }
        }
        combined_subject.append("</tr>");
        combined_lecture.append("</tr>");
        combined_class.append("</tr>");
        combined_period.append("</tr>");
        for(final int _classroom : decoded_classrooms.keySet())
        {
            combined_subject.append("<tr>");
            combined_subject.append("<th>")
                            .append(decoded_classrooms.get(_classroom).getName())
                            .append("</th>");
            combined_lecture.append("<tr>");
            combined_lecture.append("<th>")
                            .append(decoded_classrooms.get(_classroom).getName())
                            .append("</th>");
            combined_class.append("<tr>");
            combined_class.append("<th>")
                          .append(decoded_classrooms.get(_classroom).getName())
                          .append("</th>");
            combined_period.append("<tr>");
            combined_period.append("<th>")
                           .append(decoded_classrooms.get(_classroom).getName())
                           .append("</th>");
            for(final int _day : arranged_day)
            {
                combined_subject.append(query_subject.get(_classroom).get(_day));
                combined_lecture.append(query_lecture.get(_classroom).get(_day));
                combined_class.append(query_class.get(_classroom).get(_day));
                combined_period.append(query_period.get(_classroom).get(_day));
            }
            combined_subject.append("</tr>");
            combined_lecture.append("</tr>");
            combined_class.append("</tr>");
            combined_period.append("</tr>");
        }
        combined_subject.append("</table>");
        combined_lecture.append("</table>");
        combined_class.append("</table>");
        combined_period.append("</table>");
        return combined_subject.append(combined_lecture).append(combined_class).append(combined_period).toString();
    }

    @NotNull @SuppressWarnings({"ConstantConditions", "Duplicates"}) private String _formatSubject(@NotNull final Data gBest, @NotNull final DatasetGenerator dsLoader, @NotNull final DBProblemLoader dbLoader)
    {
        /*
        * Initialize Container
        * */
        @NotNull final Int2ObjectMap<Int2ObjectMap<StringBuilder>> query = new Int2ObjectLinkedOpenHashMap<>(dbLoader.getClassroomSize());

        @NotNull final DBSchool  empty_school  = new DBSchool(-1, "", "", "", "", -1, 1, 1);
        @NotNull final DBSubject empty_subject = new DBSubject(-1, "Tidak Ada", "", empty_school);
        @NotNull final DBLecture empty_lecture = new DBLecture(-1, "Tidak Ada", empty_school);
        @NotNull final DBClass   empty_class   = new DBClass(-1, "Tidak Ada", empty_school);

        @NotNull final DSLesson[]                 encoded_lessons       = dsLoader.getDataset().getLessons();
        @NotNull final Int2IntMap                 classroom_lv0_decoder = dsLoader.getDecoder().getClassrooms();
        @NotNull final Int2IntMap                 day_decoder           = dsLoader.getDecoder().getActiveDays();
        @NotNull final Int2IntMap                 period_decoder        = dsLoader.getDecoder().getActivePeriods();
        @NotNull final Int2IntMap                 subject_decoder       = dsLoader.getDecoder().getSubjects();
        @NotNull final Int2IntMap                 lecture_decoder       = dsLoader.getDecoder().getLecturers();
        @NotNull final Int2IntMap                 class_decoder         = dsLoader.getDecoder().getClasses();
        @NotNull final Int2ObjectMap<DBClassroom> decoded_classrooms    = dbLoader.getClassrooms();
        @NotNull final Int2ObjectMap<DBDay>       decoded_days          = dbLoader.getDays();
        @NotNull final Int2ObjectMap<DBSubject>   decoded_subjects      = dbLoader.getSubjects();
        @NotNull final Int2ObjectMap<DBLecture>   decoded_lecturers     = dbLoader.getLecturers();
        @NotNull final Int2ObjectMap<DBClass>     decoded_classes       = dbLoader.getClasses();
        @NotNull final Int2ObjectMap<DBPeriod>    decoded_period        = dbLoader.getPeriods();

        @NotNull final Int2ObjectMap<ColorPair> subjectColor = new Int2ObjectLinkedOpenHashMap<>(dbLoader.getSubjectSize());
        for(final int _subject : dbLoader.getSubjects().keySet())
        {
            subjectColor.put(_subject, new ColorPair(new Color(Color.HSBtoRGB((float) FastMath.random(), (float) FastMath.random(), 0.5F + ((float) FastMath.random()) / 2F))));
        }

        int i_cluster = -1;
        for(@NotNull final DSLessonCluster lesson_cluster : dsLoader.getDataset().getLessonClusters())
        {
            /*
            * Increment day index
            * */
            ++i_cluster;
            @NotNull final Int2IntMap classroom_lv1_decoder = lesson_cluster.getClassroomDecoder();

            /*
            * Get lessons data in current cluster
            * */
            final int[] lessons = gBest.getPosition(i_cluster).getPosition();

            /*
            * Initialize lesson counter
            * */
            int c_lesson = -1;

            /*
            * Get lesson according to lesson counter
            * */
            DSLesson lesson = encoded_lessons[lessons[++c_lesson]];

            /*
            * Get current lesson sks
            * */
            int lesson_sks = lesson == null ? 1 : lesson.getSks();

            /*
            * Initialize sks counter
            * */
            int c_sks = 0;

            /*
            * Foreach classroom in current cluster
            * */
            for(final int classroom : lesson_cluster.getClassrooms())
            {
                final int    decoded_classroom_id = classroom_lv0_decoder.get(classroom_lv1_decoder.get(classroom));
                final String classroom_name       = decoded_classrooms.get(decoded_classroom_id).getName();
                if(!query.containsKey(decoded_classroom_id))
                {
                    query.put(decoded_classroom_id, new Int2ObjectLinkedOpenHashMap<>(decoded_days.size()));
                }

                /*
                * Initialize day index
                * Foreach day in current classroom
                * */
                int i_day = -1;
                for(final double[] day : lesson_cluster.getClassroomsTimeoff(classroom).getTimeoff())
                {
                    /*
                    * Increment day index
                    * */
                    ++i_day;

                    final int    decoded_day_id = day_decoder.get(i_day);
                    final String day_name       = decoded_days.get(decoded_day_id).getName();

                    if(!query.get(decoded_classroom_id).containsKey(decoded_day_id))
                    {
                        query.get(decoded_classroom_id).put(decoded_day_id, new StringBuilder());
                    }
                    @SuppressWarnings("MismatchedQueryAndUpdateOfStringBuilder")
                    @NotNull final StringBuilder tmp_query = query.get(decoded_classroom_id).get(decoded_day_id);

                    /*
                    * Initialize period index
                    * Foreach all period within day
                    * */
                    int i_period = -1;
                    for(final double period : day)
                    {
                       /*
                        * Increment period index
                        * */
                        ++i_period;

                        /*
                        * If current period time off is available
                        * */
                        if(period != 0.2)
                        {
                            /*
                            * check if counter sks have same value with lesson sks
                            * */
                            if(++c_sks == lesson_sks)
                            {
                                tmp_query.append("<td class=\"tooltip\" colspan=\"")
                                         .append(lesson_sks)
                                         .append("\" style=\"color: ")
                                         .append(lesson != null ? (lesson.getSubject() == -1 ? "#000000" : ColorPair.parseColor(subjectColor.get(subject_decoder.get(lesson.getSubject())).foreground)) : "#000000")
                                         .append("; background-color: ")
                                         .append(lesson != null ? (lesson.getSubject() == -1 ? "#FFFFFF" : ColorPair.parseColor(subjectColor.get(subject_decoder.get(lesson.getSubject())).background)) : "#FFFFFF")
                                         .append("\">");
                                if(lesson != null)
                                {
                                    @NotNull final String[] splitted_class = decoded_classes.getOrDefault(class_decoder.get(lesson.getKlass()), empty_class).getName().split("-");
                                    tmp_query.append("<strong class=\"content\">")
                                             .append(decoded_subjects.getOrDefault(subject_decoder.get(lesson.getSubject()), empty_subject).getName())
                                             .append("</strong>");
                                    tmp_query.append("<br>");
                                    tmp_query.append("<span class=\"content\">")
                                             .append(decoded_lecturers.getOrDefault(lecture_decoder.get(lesson.getLecture()), empty_lecture).getName())
                                             .append("</span>");
                                    tmp_query.append("<br>");
                                    tmp_query.append("<span class=\"content\">")
                                             .append("Kelas - ")
                                             .append(splitted_class[splitted_class.length - 1])
                                             .append("</span>");
                                    tmp_query.append("<br>");
                                    tmp_query.append("<strong class=\"content-time\">")
                                             .append(decoded_period.get(period_decoder.get(i_period - (lesson_sks - 1))).getStart())
                                             .append(" - ")
                                             .append(decoded_period.get(period_decoder.get(i_period)).getEnd())
                                             .append("</strong>");
                                    tmp_query.append("<div class=\"tooltiptext\">")
                                             .append(day_name)
                                             .append(" - ")
                                             .append(classroom_name)
                                             .append("</div>");
                                }
                                tmp_query.append("</td>");

                                /*
                                * Refill current lesson data
                                * */
                                try
                                {
                                    lesson = encoded_lessons[lessons[++c_lesson]];
                                }
                                catch(ArrayIndexOutOfBoundsException ignored)
                                {
                                }
                                finally
                                {
                                    lesson_sks = lesson == null ? 1 : lesson.getSks();
                                    c_sks = 0;
                                }
                            }
                        }
                        else
                        {
                            tmp_query.append("<td class=\"tooltip _not_available\" colspan=\"").append(1).append("\" style=\"color: black\">");
                            tmp_query.append("</td>");
                        }
                    }
                }
            }
        }

        /*
        * generated arranged day
        * */
        @NotNull final int[] arranged_day = new int[decoded_days.size()];
        for(@NotNull final DBDay _day : dbLoader.getDays().values())
        {
            arranged_day[_day.getPosition() - 1] = _day.getId();
        }

        //noinspection Convert2streamapi
        for(final int _classroom : dbLoader.getClassrooms().keySet())
        {
            if(!query.containsKey(_classroom))
            {
                query.put(_classroom, new Int2ObjectLinkedOpenHashMap<>(arranged_day.length));
                for(final int _day : arranged_day)
                {
                    @NotNull final StringBuilder tmp_query = new StringBuilder();

                    //noinspection unchecked
                    for(final DBTimeOff timeoff : (ObjectList<DBTimeOff>) dbLoader.getClassroom(_classroom).getTimeoff().getAvailabilities().get(_day - 1))
                    {
                        if(timeoff.getAvailability().getId() == 1)
                        {
                            tmp_query.append("<td class=\"tooltip _not_available\" colspan=\"").append(1).append("\" style=\"color: black\">");
                            tmp_query.append("</td>");
                        }
                        else
                        {
                            tmp_query.append("<td class=\"tooltip\" colspan=\"").append(1).append("\" style=\"color: black\">");
                            tmp_query.append("</td>");
                        }
                    }
                    query.get(_classroom).put(_day, tmp_query);
                }
            }
        }

        @NotNull final StringBuilder combined = new StringBuilder();
        combined.append("<table id=\"tt_3\" width=\"")
                .append(((decoded_period.size() * decoded_days.size() + 1) * 60) + ((decoded_days.size() - 1) * 85))
                .append("px\" style=\"display:none\">");
        combined.append("<tr>");
        for(int c_d = -1, c_ds = arranged_day.length; ++c_d < c_ds; )
        {
            combined.append("<th class=\"_dummy\"></th>");
            for(int c_p = -1, c_ps = decoded_period.size(); ++c_p < c_ps; )
            {
                combined.append("<th class=\"_dummy\"></th>");
            }
        }
        combined.append("</tr>");
        combined.append("<tr>");
        combined.append("<th colspan=\"1\">Hari --> <br>Ruangan</th>");
        for(int c_d = -1, c_ds = arranged_day.length, c_ps = decoded_period.size(), c_cs = decoded_classrooms.size(); ++c_d < c_ds; )
        {
            combined.append("<th colspan=\"")
                    .append(c_ps)
                    .append("\">")
                    .append(decoded_days.get(arranged_day[c_d]).getName())
                    .append("</th>");
            if((c_d + 1) != c_ds)
            {
                combined.append("<th class=\"divider\" rowspan=\"")
                        .append(c_cs + 1)
                        .append("\"></th>");
            }
        }
        combined.append("</tr>");
        for(final int _classroom : decoded_classrooms.keySet())
        {
            combined.append("<tr>");
            combined.append("<th>")
                    .append(decoded_classrooms.get(_classroom).getName())
                    .append("</th>");
            for(final int _day : arranged_day)
            {
                combined.append(query.get(_classroom).get(_day));
            }
            combined.append("</tr>");
        }
        combined.append("</table>");
        return combined.toString();
    }

    @NotNull @SuppressWarnings({"ConstantConditions", "Duplicates"}) private String _formatClass(@NotNull final Data gBest, @NotNull final DatasetGenerator dsLoader, @NotNull final DBProblemLoader dbLoader)
    {
        /*
        * Initialize Container
        * */
        @NotNull final Int2ObjectMap<Int2ObjectMap<StringBuilder>> query = new Int2ObjectLinkedOpenHashMap<>(dbLoader.getClassroomSize());

        @NotNull final DBSchool  empty_school  = new DBSchool(-1, "", "", "", "", -1, 1, 1);
        @NotNull final DBSubject empty_subject = new DBSubject(-1, "Tidak Ada", "", empty_school);
        @NotNull final DBLecture empty_lecture = new DBLecture(-1, "Tidak Ada", empty_school);
        @NotNull final DBClass   empty_class   = new DBClass(-1, "Tidak Ada", empty_school);

        @NotNull final DSLesson[]                 encoded_lessons       = dsLoader.getDataset().getLessons();
        @NotNull final Int2IntMap                 classroom_lv0_decoder = dsLoader.getDecoder().getClassrooms();
        @NotNull final Int2IntMap                 day_decoder           = dsLoader.getDecoder().getActiveDays();
        @NotNull final Int2IntMap                 period_decoder        = dsLoader.getDecoder().getActivePeriods();
        @NotNull final Int2IntMap                 subject_decoder       = dsLoader.getDecoder().getSubjects();
        @NotNull final Int2IntMap                 lecture_decoder       = dsLoader.getDecoder().getLecturers();
        @NotNull final Int2IntMap                 class_decoder         = dsLoader.getDecoder().getClasses();
        @NotNull final Int2ObjectMap<DBClassroom> decoded_classrooms    = dbLoader.getClassrooms();
        @NotNull final Int2ObjectMap<DBDay>       decoded_days          = dbLoader.getDays();
        @NotNull final Int2ObjectMap<DBSubject>   decoded_subjects      = dbLoader.getSubjects();
        @NotNull final Int2ObjectMap<DBLecture>   decoded_lecturers     = dbLoader.getLecturers();
        @NotNull final Int2ObjectMap<DBClass>     decoded_classes       = dbLoader.getClasses();
        @NotNull final Int2ObjectMap<DBPeriod>    decoded_period        = dbLoader.getPeriods();

        @NotNull final Int2ObjectMap<ColorPair> classColor = new Int2ObjectLinkedOpenHashMap<>(dbLoader.getClassSize());
        for(final int _class : dbLoader.getClasses().keySet())
        {
            classColor.put(_class, new ColorPair(new Color(Color.HSBtoRGB((float) FastMath.random(), (float) FastMath.random(), 0.5F + ((float) FastMath.random()) / 2F))));
        }

        int i_cluster = -1;
        for(@NotNull final DSLessonCluster lesson_cluster : dsLoader.getDataset().getLessonClusters())
        {
            /*
            * Increment day index
            * */
            ++i_cluster;
            @NotNull final Int2IntMap classroom_lv1_decoder = lesson_cluster.getClassroomDecoder();

            /*
            * Get lessons data in current cluster
            * */
            final int[] lessons = gBest.getPosition(i_cluster).getPosition();

            /*
            * Initialize lesson counter
            * */
            int c_lesson = -1;

            /*
            * Get lesson according to lesson counter
            * */
            DSLesson lesson = encoded_lessons[lessons[++c_lesson]];

            /*
            * Get current lesson sks
            * */
            int lesson_sks = lesson == null ? 1 : lesson.getSks();

            /*
            * Initialize sks counter
            * */
            int c_sks = 0;

            /*
            * Foreach classroom in current cluster
            * */
            for(final int classroom : lesson_cluster.getClassrooms())
            {
                final int    decoded_classroom_id = classroom_lv0_decoder.get(classroom_lv1_decoder.get(classroom));
                final String classroom_name       = decoded_classrooms.get(decoded_classroom_id).getName();
                if(!query.containsKey(decoded_classroom_id))
                {
                    query.put(decoded_classroom_id, new Int2ObjectLinkedOpenHashMap<>(decoded_days.size()));
                }

                /*
                * Initialize day index
                * Foreach day in current classroom
                * */
                int i_day = -1;
                for(final double[] day : lesson_cluster.getClassroomsTimeoff(classroom).getTimeoff())
                {
                    /*
                    * Increment day index
                    * */
                    ++i_day;

                    final int    decoded_day_id = day_decoder.get(i_day);
                    final String day_name       = decoded_days.get(decoded_day_id).getName();

                    if(!query.get(decoded_classroom_id).containsKey(decoded_day_id))
                    {
                        query.get(decoded_classroom_id).put(decoded_day_id, new StringBuilder());
                    }
                    @SuppressWarnings("MismatchedQueryAndUpdateOfStringBuilder")
                    @NotNull final StringBuilder tmp_query = query.get(decoded_classroom_id).get(decoded_day_id);

                    /*
                    * Initialize period index
                    * Foreach all period within day
                    * */
                    int i_period = -1;
                    for(final double period : day)
                    {
                       /*
                        * Increment period index
                        * */
                        ++i_period;

                        /*
                        * If current period time off is available
                        * */
                        if(period != 0.2)
                        {
                            /*
                            * check if counter sks have same value with lesson sks
                            * */
                            if(++c_sks == lesson_sks)
                            {
                                tmp_query.append("<td class=\"tooltip\" colspan=\"")
                                         .append(lesson_sks)
                                         .append("\" style=\"color: ")
                                         .append(lesson != null ? (lesson.getKlass() == -1 ? "#000000" : ColorPair.parseColor(classColor.get(class_decoder.get(lesson.getKlass())).foreground)) : "#000000")
                                         .append("; background-color: ")
                                         .append(lesson != null ? (lesson.getKlass() == -1 ? "#FFFFFF" : ColorPair.parseColor(classColor.get(class_decoder.get(lesson.getKlass())).background)) : "#FFFFFF")
                                         .append("\">");
                                if(lesson != null)
                                {
                                    @NotNull final String[] splitted_class = decoded_classes.getOrDefault(class_decoder.get(lesson.getKlass()), empty_class).getName().split("-");
                                    tmp_query.append("<strong class=\"content\">")
                                             .append(decoded_subjects.getOrDefault(subject_decoder.get(lesson.getSubject()), empty_subject).getName())
                                             .append("</strong>");
                                    tmp_query.append("<br>");
                                    tmp_query.append("<span class=\"content\">")
                                             .append(decoded_lecturers.getOrDefault(lecture_decoder.get(lesson.getLecture()), empty_lecture).getName())
                                             .append("</span>");
                                    tmp_query.append("<br>");
                                    tmp_query.append("<span class=\"content\">")
                                             .append("Kelas - ")
                                             .append(splitted_class[splitted_class.length - 1])
                                             .append("</span>");
                                    tmp_query.append("<br>");
                                    tmp_query.append("<strong class=\"content-time\">")
                                             .append(decoded_period.get(period_decoder.get(i_period - (lesson_sks - 1))).getStart())
                                             .append(" - ")
                                             .append(decoded_period.get(period_decoder.get(i_period)).getEnd())
                                             .append("</strong>");
                                    tmp_query.append("<div class=\"tooltiptext\">")
                                             .append(day_name)
                                             .append(" - ")
                                             .append(classroom_name)
                                             .append("</div>");
                                }
                                tmp_query.append("</td>");

                                /*
                                * Refill current lesson data
                                * */
                                try
                                {
                                    lesson = encoded_lessons[lessons[++c_lesson]];
                                }
                                catch(ArrayIndexOutOfBoundsException ignored)
                                {
                                }
                                finally
                                {
                                    lesson_sks = lesson == null ? 1 : lesson.getSks();
                                    c_sks = 0;
                                }
                            }
                        }
                        else
                        {
                            tmp_query.append("<td class=\"tooltip _not_available\" colspan=\"").append(1).append("\" style=\"color: black\">");
                            tmp_query.append("</td>");
                        }
                    }
                }
            }
        }

        /*
        * generated arranged day
        * */
        @NotNull final int[] arranged_day = new int[decoded_days.size()];
        for(@NotNull final DBDay _day : dbLoader.getDays().values())
        {
            arranged_day[_day.getPosition() - 1] = _day.getId();
        }

        //noinspection Convert2streamapi
        for(final int _classroom : dbLoader.getClassrooms().keySet())
        {
            if(!query.containsKey(_classroom))
            {
                query.put(_classroom, new Int2ObjectLinkedOpenHashMap<>(arranged_day.length));
                for(final int _day : arranged_day)
                {
                    @NotNull final StringBuilder tmp_query = new StringBuilder();

                    //noinspection unchecked
                    for(final DBTimeOff timeoff : (ObjectList<DBTimeOff>) dbLoader.getClassroom(_classroom).getTimeoff().getAvailabilities().get(_day - 1))
                    {
                        if(timeoff.getAvailability().getId() == 1)
                        {
                            tmp_query.append("<td class=\"tooltip _not_available\" colspan=\"").append(1).append("\" style=\"color: black\">");
                            tmp_query.append("</td>");
                        }
                        else
                        {
                            tmp_query.append("<td class=\"tooltip\" colspan=\"").append(1).append("\" style=\"color: black\">");
                            tmp_query.append("</td>");
                        }
                    }
                    query.get(_classroom).put(_day, tmp_query);
                }
            }
        }

        @NotNull final StringBuilder combined = new StringBuilder();
        combined.append("<table id=\"tt_2\" width=\"")
                .append(((decoded_period.size() * decoded_days.size() + 1) * 60) + ((decoded_days.size() - 1) * 85))
                .append("px\" style=\"display:none\">");
        combined.append("<tr>");
        for(int c_d = -1, c_ds = arranged_day.length; ++c_d < c_ds; )
        {
            combined.append("<th class=\"_dummy\"></th>");
            for(int c_p = -1, c_ps = decoded_period.size(); ++c_p < c_ps; )
            {
                combined.append("<th class=\"_dummy\"></th>");
            }
        }
        combined.append("</tr>");
        combined.append("<tr>");
        combined.append("<th colspan=\"1\">Hari --> <br>Ruangan</th>");
        for(int c_d = -1, c_ds = arranged_day.length, c_ps = decoded_period.size(), c_cs = decoded_classrooms.size(); ++c_d < c_ds; )
        {
            combined.append("<th colspan=\"")
                    .append(c_ps)
                    .append("\">")
                    .append(decoded_days.get(arranged_day[c_d]).getName())
                    .append("</th>");
            if((c_d + 1) != c_ds)
            {
                combined.append("<th class=\"divider\" rowspan=\"")
                        .append(c_cs + 1)
                        .append("\"></th>");
            }
        }
        combined.append("</tr>");
        for(final int _classroom : decoded_classrooms.keySet())
        {
            combined.append("<tr>");
            combined.append("<th>")
                    .append(decoded_classrooms.get(_classroom).getName())
                    .append("</th>");
            for(final int _day : arranged_day)
            {
                combined.append(query.get(_classroom).get(_day));
            }
            combined.append("</tr>");
        }
        combined.append("</table>");
        return combined.toString();
    }

    @NotNull @SuppressWarnings({"ConstantConditions", "Duplicates"}) private String _formatLecture(@NotNull final Data gBest, @NotNull final DatasetGenerator dsLoader, @NotNull final DBProblemLoader dbLoader)
    {
        /*
        * Initialize Container
        * */
        @NotNull final Int2ObjectMap<Int2ObjectMap<StringBuilder>> query = new Int2ObjectLinkedOpenHashMap<>(dbLoader.getClassroomSize());

        @NotNull final DBSchool  empty_school  = new DBSchool(-1, "", "", "", "", -1, 1, 1);
        @NotNull final DBSubject empty_subject = new DBSubject(-1, "Tidak Ada", "", empty_school);
        @NotNull final DBLecture empty_lecture = new DBLecture(-1, "Tidak Ada", empty_school);
        @NotNull final DBClass   empty_class   = new DBClass(-1, "Tidak Ada", empty_school);

        @NotNull final DSLesson[]                 encoded_lessons       = dsLoader.getDataset().getLessons();
        @NotNull final Int2IntMap                 classroom_lv0_decoder = dsLoader.getDecoder().getClassrooms();
        @NotNull final Int2IntMap                 day_decoder           = dsLoader.getDecoder().getActiveDays();
        @NotNull final Int2IntMap                 period_decoder        = dsLoader.getDecoder().getActivePeriods();
        @NotNull final Int2IntMap                 subject_decoder       = dsLoader.getDecoder().getSubjects();
        @NotNull final Int2IntMap                 lecture_decoder       = dsLoader.getDecoder().getLecturers();
        @NotNull final Int2IntMap                 class_decoder         = dsLoader.getDecoder().getClasses();
        @NotNull final Int2ObjectMap<DBClassroom> decoded_classrooms    = dbLoader.getClassrooms();
        @NotNull final Int2ObjectMap<DBDay>       decoded_days          = dbLoader.getDays();
        @NotNull final Int2ObjectMap<DBSubject>   decoded_subjects      = dbLoader.getSubjects();
        @NotNull final Int2ObjectMap<DBLecture>   decoded_lecturers     = dbLoader.getLecturers();
        @NotNull final Int2ObjectMap<DBClass>     decoded_classes       = dbLoader.getClasses();
        @NotNull final Int2ObjectMap<DBPeriod>    decoded_period        = dbLoader.getPeriods();

        @NotNull final Int2ObjectMap<ColorPair> lectureColor = new Int2ObjectLinkedOpenHashMap<>(dbLoader.getLectureSize());
        for(final int _lecturer : dbLoader.getLecturers().keySet())
        {
            lectureColor.put(_lecturer, new ColorPair(new Color(Color.HSBtoRGB((float) FastMath.random(), (float) FastMath.random(), 0.5F + ((float) FastMath.random()) / 2F))));
        }

        int i_cluster = -1;
        for(@NotNull final DSLessonCluster lesson_cluster : dsLoader.getDataset().getLessonClusters())
        {
            /*
            * Increment day index
            * */
            ++i_cluster;
            @NotNull final Int2IntMap classroom_lv1_decoder = lesson_cluster.getClassroomDecoder();

            /*
            * Get lessons data in current cluster
            * */
            final int[] lessons = gBest.getPosition(i_cluster).getPosition();

            /*
            * Initialize lesson counter
            * */
            int c_lesson = -1;

            /*
            * Get lesson according to lesson counter
            * */
            DSLesson lesson = encoded_lessons[lessons[++c_lesson]];

            /*
            * Get current lesson sks
            * */
            int lesson_sks = lesson == null ? 1 : lesson.getSks();

            /*
            * Initialize sks counter
            * */
            int c_sks = 0;

            /*
            * Foreach classroom in current cluster
            * */
            for(final int classroom : lesson_cluster.getClassrooms())
            {
                final int    decoded_classroom_id = classroom_lv0_decoder.get(classroom_lv1_decoder.get(classroom));
                final String classroom_name       = decoded_classrooms.get(decoded_classroom_id).getName();
                if(!query.containsKey(decoded_classroom_id))
                {
                    query.put(decoded_classroom_id, new Int2ObjectLinkedOpenHashMap<>(decoded_days.size()));
                }

                /*
                * Initialize day index
                * Foreach day in current classroom
                * */
                int i_day = -1;
                for(final double[] day : lesson_cluster.getClassroomsTimeoff(classroom).getTimeoff())
                {
                    /*
                    * Increment day index
                    * */
                    ++i_day;

                    final int    decoded_day_id = day_decoder.get(i_day);
                    final String day_name       = decoded_days.get(decoded_day_id).getName();

                    if(!query.get(decoded_classroom_id).containsKey(decoded_day_id))
                    {
                        query.get(decoded_classroom_id).put(decoded_day_id, new StringBuilder());
                    }
                    @SuppressWarnings("MismatchedQueryAndUpdateOfStringBuilder")
                    @NotNull final StringBuilder tmp_query = query.get(decoded_classroom_id).get(decoded_day_id);

                    /*
                    * Initialize period index
                    * Foreach all period within day
                    * */
                    int i_period = -1;
                    for(final double period : day)
                    {
                       /*
                        * Increment period index
                        * */
                        ++i_period;

                        /*
                        * If current period time off is available
                        * */
                        if(period != 0.2)
                        {
                            /*
                            * check if counter sks have same value with lesson sks
                            * */
                            if(++c_sks == lesson_sks)
                            {
                                tmp_query.append("<td class=\"tooltip\" colspan=\"")
                                         .append(lesson_sks)
                                         .append("\" style=\"color: ")
                                         .append(lesson != null ? (lesson.getLecture() == -1 ? "#000000" : ColorPair.parseColor(lectureColor.get(lecture_decoder.get(lesson.getLecture())).foreground)) : "#000000")
                                         .append("; background-color: ")
                                         .append(lesson != null ? (lesson.getLecture() == -1 ? "#FFFFFF" : ColorPair.parseColor(lectureColor.get(lecture_decoder.get(lesson.getLecture())).background)) : "#FFFFFF")
                                         .append("\">");
                                if(lesson != null)
                                {
                                    @NotNull final String[] splitted_class = decoded_classes.getOrDefault(class_decoder.get(lesson.getKlass()), empty_class).getName().split("-");
                                    tmp_query.append("<strong class=\"content\">")
                                             .append(decoded_subjects.getOrDefault(subject_decoder.get(lesson.getSubject()), empty_subject).getName())
                                             .append("</strong>");
                                    tmp_query.append("<br>");
                                    tmp_query.append("<span class=\"content\">")
                                             .append(decoded_lecturers.getOrDefault(lecture_decoder.get(lesson.getLecture()), empty_lecture).getName())
                                             .append("</span>");
                                    tmp_query.append("<br>");
                                    tmp_query.append("<span class=\"content\">")
                                             .append("Kelas - ")
                                             .append(splitted_class[splitted_class.length - 1])
                                             .append("</span>");
                                    tmp_query.append("<br>");
                                    tmp_query.append("<strong class=\"content-time\">")
                                             .append(decoded_period.get(period_decoder.get(i_period - (lesson_sks - 1))).getStart())
                                             .append(" - ")
                                             .append(decoded_period.get(period_decoder.get(i_period)).getEnd())
                                             .append("</strong>");
                                    tmp_query.append("<div class=\"tooltiptext\">")
                                             .append(day_name)
                                             .append(" - ")
                                             .append(classroom_name)
                                             .append("</div>");
                                }
                                tmp_query.append("</td>");

                                /*
                                * Refill current lesson data
                                * */
                                try
                                {
                                    lesson = encoded_lessons[lessons[++c_lesson]];
                                }
                                catch(ArrayIndexOutOfBoundsException ignored)
                                {
                                }
                                finally
                                {
                                    lesson_sks = lesson == null ? 1 : lesson.getSks();
                                    c_sks = 0;
                                }
                            }
                        }
                        else
                        {
                            tmp_query.append("<td class=\"tooltip _not_available\" colspan=\"").append(1).append("\" style=\"color: black\">");
                            tmp_query.append("</td>");
                        }
                    }
                }
            }
        }

        /*
        * generated arranged day
        * */
        @NotNull final int[] arranged_day = new int[decoded_days.size()];
        for(@NotNull final DBDay _day : dbLoader.getDays().values())
        {
            arranged_day[_day.getPosition() - 1] = _day.getId();
        }

        //noinspection Convert2streamapi
        for(final int _classroom : dbLoader.getClassrooms().keySet())
        {
            if(!query.containsKey(_classroom))
            {
                query.put(_classroom, new Int2ObjectLinkedOpenHashMap<>(arranged_day.length));
                for(final int _day : arranged_day)
                {
                    @NotNull final StringBuilder tmp_query = new StringBuilder();

                    //noinspection unchecked
                    for(final DBTimeOff timeoff : (ObjectList<DBTimeOff>) dbLoader.getClassroom(_classroom).getTimeoff().getAvailabilities().get(_day - 1))
                    {
                        if(timeoff.getAvailability().getId() == 1)
                        {
                            tmp_query.append("<td class=\"tooltip _not_available\" colspan=\"").append(1).append("\" style=\"color: black\">");
                            tmp_query.append("</td>");
                        }
                        else
                        {
                            tmp_query.append("<td class=\"tooltip\" colspan=\"").append(1).append("\" style=\"color: black\">");
                            tmp_query.append("</td>");
                        }
                    }
                    query.get(_classroom).put(_day, tmp_query);
                }
            }
        }

        @NotNull final StringBuilder combined = new StringBuilder();
        combined.append("<table id=\"tt_1\" width=\"")
                .append(((decoded_period.size() * decoded_days.size() + 1) * 60) + ((decoded_days.size() - 1) * 85))
                .append("px\" style=\"display:none\">");
        combined.append("<tr>");
        for(int c_d = -1, c_ds = arranged_day.length; ++c_d < c_ds; )
        {
            combined.append("<th class=\"_dummy\"></th>");
            for(int c_p = -1, c_ps = decoded_period.size(); ++c_p < c_ps; )
            {
                combined.append("<th class=\"_dummy\"></th>");
            }
        }
        combined.append("</tr>");
        combined.append("<tr>");
        combined.append("<th colspan=\"1\">Hari --> <br>Ruangan</th>");
        for(int c_d = -1, c_ds = arranged_day.length, c_ps = decoded_period.size(), c_cs = decoded_classrooms.size(); ++c_d < c_ds; )
        {
            combined.append("<th colspan=\"")
                    .append(c_ps)
                    .append("\">")
                    .append(decoded_days.get(arranged_day[c_d]).getName())
                    .append("</th>");
            if((c_d + 1) != c_ds)
            {
                combined.append("<th class=\"divider\" rowspan=\"")
                        .append(c_cs + 1)
                        .append("\"></th>");
            }
        }
        combined.append("</tr>");
        for(final int _classroom : decoded_classrooms.keySet())
        {
            combined.append("<tr>");
            combined.append("<th>")
                    .append(decoded_classrooms.get(_classroom).getName())
                    .append("</th>");
            for(final int _day : arranged_day)
            {
                combined.append(query.get(_classroom).get(_day));
            }
            combined.append("</tr>");
        }
        combined.append("</table>");
        return combined.toString();
    }

    @NotNull @SuppressWarnings({"ConstantConditions", "Duplicates"}) private String _formatLesson(@NotNull final Data gBest, @NotNull final DatasetGenerator dsLoader, @NotNull final DBProblemLoader dbLoader)
    {
        /*
        * Initialize Container
        * */
        @NotNull final Int2ObjectMap<Int2ObjectMap<StringBuilder>> query = new Int2ObjectLinkedOpenHashMap<>(dbLoader.getClassroomSize());

        @NotNull final DBSchool  empty_school  = new DBSchool(-1, "", "", "", "", -1, 1, 1);
        @NotNull final DBSubject empty_subject = new DBSubject(-1, "Tidak Ada", "", empty_school);
        @NotNull final DBLecture empty_lecture = new DBLecture(-1, "Tidak Ada", empty_school);
        @NotNull final DBClass   empty_class   = new DBClass(-1, "Tidak Ada", empty_school);

        @NotNull final DSLesson[]                 encoded_lessons       = dsLoader.getDataset().getLessons();
        @NotNull final Int2IntMap                 classroom_lv0_decoder = dsLoader.getDecoder().getClassrooms();
        @NotNull final Int2IntMap                 day_decoder           = dsLoader.getDecoder().getActiveDays();
        @NotNull final Int2IntMap                 period_decoder        = dsLoader.getDecoder().getActivePeriods();
        @NotNull final Int2IntMap                 subject_decoder       = dsLoader.getDecoder().getSubjects();
        @NotNull final Int2IntMap                 lecturer_decoder      = dsLoader.getDecoder().getLecturers();
        @NotNull final Int2IntMap                 classes_decoder       = dsLoader.getDecoder().getClasses();
        @NotNull final Int2IntMap                 lesson_decoder        = dsLoader.getDecoder().getLessons();
        @NotNull final Int2ObjectMap<DBClassroom> decoded_classrooms    = dbLoader.getClassrooms();
        @NotNull final Int2ObjectMap<DBDay>       decoded_days          = dbLoader.getDays();
        @NotNull final Int2ObjectMap<DBSubject>   decoded_subjects      = dbLoader.getSubjects();
        @NotNull final Int2ObjectMap<DBLecture>   decoded_lecturers     = dbLoader.getLecturers();
        @NotNull final Int2ObjectMap<DBClass>     decoded_classes       = dbLoader.getClasses();
        @NotNull final Int2ObjectMap<DBPeriod>    decoded_period        = dbLoader.getPeriods();

        @NotNull final Int2ObjectMap<ColorPair> lessonColor = new Int2ObjectLinkedOpenHashMap<>(dbLoader.getLessonSize());
        for(final int _lesson : dbLoader.getLessons().keySet())
        {
            lessonColor.put(_lesson, new ColorPair(new Color(Color.HSBtoRGB((float) FastMath.random(), (float) FastMath.random(), 0.5F + ((float) FastMath.random()) / 2F))));
        }

        int i_cluster = -1;
        for(@NotNull final DSLessonCluster lesson_cluster : dsLoader.getDataset().getLessonClusters())
        {
            /*
            * Increment day index
            * */
            ++i_cluster;
            @NotNull final Int2IntMap classroom_lv1_decoder = lesson_cluster.getClassroomDecoder();

            /*
            * Get lessons data in current cluster
            * */
            final int[] lessons = gBest.getPosition(i_cluster).getPosition();

            /*
            * Initialize lesson counter
            * */
            int c_lesson = -1;

            /*
            * Get lesson according to lesson counter
            * */
            DSLesson lesson = encoded_lessons[lessons[++c_lesson]];

            /*
            * Get current lesson sks
            * */
            int lesson_sks = lesson == null ? 1 : lesson.getSks();

            /*
            * Initialize sks counter
            * */
            int c_sks = 0;

            /*
            * Foreach classroom in current cluster
            * */
            for(final int classroom : lesson_cluster.getClassrooms())
            {
                final int    decoded_classroom_id = classroom_lv0_decoder.get(classroom_lv1_decoder.get(classroom));
                final String classroom_name       = decoded_classrooms.get(decoded_classroom_id).getName();
                if(!query.containsKey(decoded_classroom_id))
                {
                    query.put(decoded_classroom_id, new Int2ObjectLinkedOpenHashMap<>(decoded_days.size()));
                }

                /*
                * Initialize day index
                * Foreach day in current classroom
                * */
                int i_day = -1;
                for(final double[] day : lesson_cluster.getClassroomsTimeoff(classroom).getTimeoff())
                {
                    /*
                    * Increment day index
                    * */
                    ++i_day;

                    final int    decoded_day_id = day_decoder.get(i_day);
                    final String day_name       = decoded_days.get(decoded_day_id).getName();

                    if(!query.get(decoded_classroom_id).containsKey(decoded_day_id))
                    {
                        query.get(decoded_classroom_id).put(decoded_day_id, new StringBuilder());
                    }
                    @SuppressWarnings("MismatchedQueryAndUpdateOfStringBuilder")
                    @NotNull final StringBuilder tmp_query = query.get(decoded_classroom_id).get(decoded_day_id);

                    /*
                    * Initialize period index
                    * Foreach all period within day
                    * */
                    int i_period = -1;
                    for(final double period : day)
                    {
                       /*
                        * Increment period index
                        * */
                        ++i_period;

                        /*
                        * If current period time off is available
                        * */
                        if(period != 0.2)
                        {
                            /*
                            * check if counter sks have same value with lesson sks
                            * */
                            if(++c_sks == lesson_sks)
                            {
                                tmp_query.append("<td class=\"tooltip\" colspan=\"")
                                         .append(lesson_sks)
                                         .append("\" style=\"color: ")
                                         .append(lesson != null ? (lesson.getLessonParent() == -1 ? ColorPair.parseColor(lessonColor.get(lesson_decoder.get(lessons[c_lesson])).foreground) : ColorPair.parseColor(lessonColor.get(lesson_decoder.get(lesson.getLessonParent())).foreground)) : "#000000")
                                         .append("; background-color: ")
                                         .append(lesson != null ? (lesson.getLessonParent() == -1 ? ColorPair.parseColor(lessonColor.get(lesson_decoder.get(lessons[c_lesson])).background) : ColorPair.parseColor(lessonColor.get(lesson_decoder.get(lesson.getLessonParent())).background)) : "#FFFFFF")
                                         .append("\">");
                                if(lesson != null)
                                {
                                    @NotNull final String[] splitted_class = decoded_classes.getOrDefault(classes_decoder.get(lesson.getKlass()), empty_class).getName().split("-");
                                    tmp_query.append("<strong class=\"content\">")
                                             .append(decoded_subjects.getOrDefault(subject_decoder.get(lesson.getSubject()), empty_subject).getName())
                                             .append("</strong>");
                                    tmp_query.append("<br>");
                                    tmp_query.append("<span class=\"content\">")
                                             .append(decoded_lecturers.getOrDefault(lecturer_decoder.get(lesson.getLecture()), empty_lecture).getName())
                                             .append("</span>");
                                    tmp_query.append("<br>");
                                    tmp_query.append("<span class=\"content\">")
                                             .append("Kelas - ")
                                             .append(splitted_class[splitted_class.length - 1])
                                             .append("</span>");
                                    tmp_query.append("<br>");
                                    tmp_query.append("<strong class=\"content-time\">")
                                             .append(decoded_period.get(period_decoder.get(i_period - (lesson_sks - 1))).getStart())
                                             .append(" - ")
                                             .append(decoded_period.get(period_decoder.get(i_period)).getEnd())
                                             .append("</strong>");
                                    tmp_query.append("<div class=\"tooltiptext\">")
                                             .append(day_name)
                                             .append(" - ")
                                             .append(classroom_name)
                                             .append("</div>");
                                }
                                tmp_query.append("</td>");

                                /*
                                * Refill current lesson data
                                * */
                                try
                                {
                                    lesson = encoded_lessons[lessons[++c_lesson]];
                                }
                                catch(ArrayIndexOutOfBoundsException ignored)
                                {
                                }
                                finally
                                {
                                    lesson_sks = lesson == null ? 1 : lesson.getSks();
                                    c_sks = 0;
                                }
                            }
                        }
                        else
                        {
                            tmp_query.append("<td class=\"tooltip _not_available\" colspan=\"").append(1).append("\" style=\"color: black\">");
                            tmp_query.append("</td>");
                        }
                    }
                }
            }
        }

        /*
        * generated arranged day
        * */
        @NotNull final int[] arranged_day = new int[decoded_days.size()];
        for(@NotNull final DBDay _day : dbLoader.getDays().values())
        {
            arranged_day[_day.getPosition() - 1] = _day.getId();
        }

        //noinspection Convert2streamapi
        for(final int _classroom : dbLoader.getClassrooms().keySet())
        {
            if(!query.containsKey(_classroom))
            {
                query.put(_classroom, new Int2ObjectLinkedOpenHashMap<>(arranged_day.length));
                for(final int _day : arranged_day)
                {
                    @NotNull final StringBuilder tmp_query = new StringBuilder();

                    //noinspection unchecked
                    for(final DBTimeOff timeoff : (ObjectList<DBTimeOff>) dbLoader.getClassroom(_classroom).getTimeoff().getAvailabilities().get(_day - 1))
                    {
                        if(timeoff.getAvailability().getId() == 1)
                        {
                            tmp_query.append("<td class=\"tooltip _not_available\" colspan=\"").append(1).append("\" style=\"color: black\">");
                            tmp_query.append("</td>");
                        }
                        else
                        {
                            tmp_query.append("<td class=\"tooltip\" colspan=\"").append(1).append("\" style=\"color: black\">");
                            tmp_query.append("</td>");
                        }
                    }
                    query.get(_classroom).put(_day, tmp_query);
                }
            }
        }

        @NotNull final StringBuilder combined = new StringBuilder();
        combined.append("<table id=\"tt_0\" width=\"")
                .append(((decoded_period.size() * decoded_days.size() + 1) * 60) + ((decoded_days.size() - 1) * 85))
                .append("px\" style=\"display:none\">");
        combined.append("<tr>");
        for(int c_d = -1, c_ds = arranged_day.length; ++c_d < c_ds; )
        {
            combined.append("<th class=\"_dummy\"></th>");
            for(int c_p = -1, c_ps = decoded_period.size(); ++c_p < c_ps; )
            {
                combined.append("<th class=\"_dummy\"></th>");
            }
        }
        combined.append("</tr>");
        combined.append("<tr>");
        combined.append("<th colspan=\"1\">Hari --> <br>Ruangan</th>");
        for(int c_d = -1, c_ds = arranged_day.length, c_ps = decoded_period.size(), c_cs = decoded_classrooms.size(); ++c_d < c_ds; )
        {
            combined.append("<th colspan=\"")
                    .append(c_ps)
                    .append("\">")
                    .append(decoded_days.get(arranged_day[c_d]).getName())
                    .append("</th>");
            if((c_d + 1) != c_ds)
            {
                combined.append("<th class=\"divider\" rowspan=\"")
                        .append(c_cs + 1)
                        .append("\"></th>");
            }
        }
        combined.append("</tr>");
        for(final int _classroom : decoded_classrooms.keySet())
        {
            combined.append("<tr>");
            combined.append("<th>")
                    .append(decoded_classrooms.get(_classroom).getName())
                    .append("</th>");
            for(final int _day : arranged_day)
            {
                combined.append(query.get(_classroom).get(_day));
            }
            combined.append("</tr>");
        }
        combined.append("</table>");
        return combined.toString();
    }

    /**
     * @link http://stackoverflow.com/questions/3942878/how-to-decide-font-color-in-white-or-black-depending-on-background-color
     */
    private static class ColorPair
    {
        private final @NotNull Color background;
        private final @NotNull Color foreground;

        private ColorPair(@NotNull Color background)
        {
            this.background = background;
            this.foreground = ColorPair.generateForeground(background);
        }

        static Color generateForeground(@NotNull final Color background)
        {
            return getOppositeColor(
                    calculateLightness(
                            adjustCComponent(background.getRed() / 255.0),
                            adjustCComponent(background.getGreen() / 255.0),
                            adjustCComponent(background.getBlue() / 255.0)));
        }

        @Contract(pure = true) private static Color getOppositeColor(double lightness)
        {
            return (lightness > 0.179) ? Color.BLACK : Color.WHITE;
        }

        @Contract(pure = true) private static double calculateLightness(double r, double g, double b)
        {
            return (0.2126 * r) + (0.7152 * g) + (0.0722 * b);
        }

        private static double adjustCComponent(double colorVal)
        {
            if(colorVal <= 0.03928)
            {
                return colorVal / 12.92;
            }
            else
            {
                return FastMath.pow((colorVal + 0.055) / 1.055, 2.4);
            }
        }

        private static String parseColor(@NotNull final Color color)
        {
            return String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
        }
    }
}