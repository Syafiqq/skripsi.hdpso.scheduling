package controller.menu;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.net.URL;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.web.WebView;
import model.database.component.DBClass;
import model.database.component.DBClassroom;
import model.database.component.DBDay;
import model.database.component.DBLecture;
import model.database.component.DBPeriod;
import model.database.component.DBSchool;
import model.database.component.DBSubject;
import model.database.component.DBTimeOff;
import model.database.loader.DBProblemLoader;
import model.dataset.component.DSLesson;
import model.dataset.component.DSLessonCluster;
import model.dataset.loader.DatasetGenerator;
import model.method.pso.hdpso.component.Data;
import model.method.pso.hdpso.component.Particle;
import model.method.pso.hdpso.component.Position;
import model.method.pso.hdpso.component.Setting;
import model.method.pso.hdpso.core.PSO;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * This <skripsi.hdpso.scheduling> project in package <controller.menu> created by : 
 * Name         : syafiq
 * Date / Time  : 01 January 2017, 4:29 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class CMCategory implements Initializable
{
    @Nullable private final Observer content;
    @FXML public            Button   mc_button_generate;

    public CMCategory()
    {
        this.content = null;
    }

    public CMCategory(@Nullable Observer rootContentCallback)
    {
        this.content = rootContentCallback;
    }

    @Override public void initialize(URL location, ResourceBundle resources)
    {

    }

    public void onMenuCategoryGenerateClick()
    {
        Executors.newSingleThreadExecutor().execute(() ->
        {
            @NotNull final DBSchool        school   = new DBSchool(1, "Program Teknologi Informasi dan Ilmu Komputer Universitas Brawijaya", "PTIIK UB", "Jl. Veteran No.8, Ketawanggede, Kec. Lowokwaru, Kota Malang, Jawa Timur", "2013 - 2014", 0, 17, 5);
            @NotNull final DBProblemLoader dbLoader = new DBProblemLoader(school);
            dbLoader.loadData();
            @NotNull final DatasetGenerator dsLoader = new DatasetGenerator(dbLoader);
            @NotNull final Setting          setting  = Setting.getInstance();
            setting.setbGlobMin(0.4);
            setting.setbGlobMax(0.6);
            setting.setbLocMin(0.100);
            setting.setbLocMax(0.4);
            setting.setbRandMin(0.001);
            setting.setbRandMax(0.1);
            setting.setMaxParticle(1);
            setting.setMaxEpoch(1000);
            setting.setTotalCore(4);

            @NotNull final PSO pso = new PSO(dsLoader);
            pso.initialize();
            Position.replace(pso.getParticle(0).getData().getPosition(0), new Position(new int[] {691, 692, 693, 694, 695, 696, 697, 698, 699, 700, 701, 702, 703, 704, 705, 706, 707, 708, 709, 710, 711, 712, 713, 714, 715, 716, 717, 718, 719, 720, 721, 722, 723, 724, 725, 726, 727, 728, 729, 730, 731, 732, 733, 734, 735, 256, 251, 253, 258, 252, 250, 736, 737, 738, 254, 739, 249, 740, 255, 257, 741, 742, 743, 744}));
            Position.replace(pso.getParticle(0).getData().getPosition(1), new Position(new int[] {545, 779, 547, 780, 548, 583, 546, 781, 782, 783, 582, 649, 647, 586, 549, 587, 784, 785, 786, 648, 685, 643, 645, 584, 650, 787, 788, 789, 689, 686, 690, 790, 651, 585, 791, 792, 793, 794, 795, 688, 644, 687, 646, 652, 796, 797, 798}));
            Position.replace(pso.getParticle(0).getData().getPosition(2), new Position(new int[] {799, 579, 578, 800, 801, 802, 803, 804, 805, 806, 807, 808, 809, 810, 811, 812, 813, 581, 814, 815, 816, 817, 818, 819, 820, 821, 822, 682, 823, 681, 824, 825, 826, 827, 828, 829, 830, 831, 832, 833, 580, 834, 684, 835, 836, 837, 838, 839, 840, 841, 842, 843, 844, 683, 845, 846, 847, 848, 849, 850, 851, 852, 853, 854, 855, 856}));
            Position.replace(pso.getParticle(0).getData().getPosition(3), new Position(new int[] {305, 301, 313, 7, 304, 12, 745, 746, 747, 310, 309, 303, 17, 748, 749, 21, 750, 751, 752, 302, 299, 306, 6, 69, 75, 753, 754, 755, 308, 312, 311, 23, 1, 2, 756, 757, 758, 759, 307, 300, 10, 25, 70, 760, 761, 762, 79, 20, 82, 4, 83, 26, 763, 764, 765, 24, 15, 5, 22, 16, 81, 766, 767, 768, 19, 74, 11, 8, 72, 0, 769, 770, 771, 14, 77, 80, 78, 13, 76, 772, 773, 774, 9, 775, 18, 71, 73, 3, 776, 777, 778}));
            Position.replace(pso.getParticle(0).getData().getPosition(4), new Position(new int[] {447, 857, 356, 467, 278, 367, 446, 180, 218, 85, 205, 477, 96, 84, 234, 565, 141, 858, 859, 860, 329, 861, 391, 409, 562, 378, 862, 559, 568, 555, 413, 169, 221, 177, 98, 379, 516, 183, 863, 864, 865, 866, 168, 542, 867, 441, 86, 658, 119, 445, 131, 474, 412, 270, 375, 222, 209, 519, 868, 151, 208, 139, 869, 217, 289, 263, 870, 146, 172, 394, 541, 395, 95, 382, 374, 133, 444, 334, 54, 465, 132, 67, 224, 437, 110, 332, 452, 426, 396, 103, 871, 872, 370, 93, 295, 327, 526, 87, 873, 130, 50, 330, 443, 51, 275, 68, 488, 326, 389, 874, 490, 348, 875, 539, 368, 561, 97, 876, 274, 153, 451, 427, 210, 60, 505, 164, 558, 349, 877, 55, 219, 434, 324, 41, 408, 489, 402, 510, 466, 453, 450, 262, 440, 640, 878, 279, 226, 411, 64, 879, 354, 112, 421, 40, 340, 400, 178, 880, 554, 550, 388, 399, 442, 405, 223, 556, 557, 140, 881, 271, 343, 882, 525, 281, 155, 383, 504, 432, 883, 66, 362, 884, 149, 478, 363, 660, 397, 407, 292, 331, 534, 885, 206, 571, 572, 624, 614, 49, 886, 887, 328, 515, 508, 888, 259, 674, 355, 606, 540, 380, 889, 570, 392, 553, 280, 890, 575, 891, 214, 502, 535, 150, 892, 893, 894, 424, 286, 469, 524, 895, 896, 897, 898, 236, 233, 460, 487, 899, 900, 901, 211, 59, 105, 485, 902, 903, 904, 30, 633, 366, 61, 905, 906, 907, 496, 530, 528, 436, 908, 909, 910, 911, 912, 913, 238, 914, 628, 626, 161, 495, 915, 916, 917, 339, 288, 136, 176, 918, 919, 920, 269, 344, 116, 335, 921, 922, 923, 376, 551, 42, 372, 924, 925, 926, 603, 536, 544, 523, 927, 928, 929, 930, 931, 932, 933, 576, 321, 934, 135, 225, 935, 936, 937, 357, 419, 938, 242, 336, 939, 940, 941, 200, 322, 942, 212, 415, 943, 944, 945, 100, 347, 552, 361, 946, 947, 948, 949, 113, 32, 134, 475, 950, 951, 952, 287, 235, 194, 138, 191, 953, 954, 955, 35, 338, 108, 410, 956, 957, 958, 184, 377, 272, 106, 959, 960, 961, 464, 507, 198, 145, 148, 962, 963, 964, 499, 323, 965, 966, 967, 533, 497, 968, 969, 970, 342, 527, 500, 27, 298, 971, 972, 973, 352, 154, 315, 403, 974, 975, 976, 673, 608, 503, 195, 162, 147, 977, 978, 979, 662, 472, 90, 560, 980, 981, 982, 983, 46, 325, 458, 492, 984, 985, 986, 987, 31, 111, 621, 47, 988, 989, 990, 991, 88, 36, 428, 511, 992, 993, 994, 995, 213, 425, 244, 390, 996, 997, 998, 593, 345, 999, 92, 566, 1000, 1001, 1002, 34, 124, 423, 422, 1003, 1004, 1005, 216, 129, 266, 29, 1006, 1007, 1008, 494, 601, 509, 573, 448, 1009, 1010, 1011, 1012, 230, 290, 189, 543, 564, 1013, 1014, 1015, 121, 563, 416, 165, 1016, 1017, 1018, 227, 473, 1019, 170, 514, 1020, 1021, 1022, 318, 314, 449, 44, 1023, 1024, 1025, 276, 353, 115, 65, 1026, 1027, 1028, 239, 479, 89, 163, 1029, 1030, 1031, 1032, 518, 398, 661, 537, 190, 1033, 1034, 1035, 346, 461, 438, 468, 1036, 1037, 1038, 56, 457, 53, 260, 1039, 1040, 1041, 199, 538, 616, 679, 171, 1042, 1043, 1044, 1045, 592, 125, 1046, 517, 1047, 45, 1048, 1049, 1050, 291, 569, 638, 665, 520, 1051, 1052, 1053, 641, 1054, 128, 414, 38, 1055, 1056, 1057, 241, 1058, 602, 317, 484, 1059, 1060, 1061, 333, 248, 498, 120, 1062, 1063, 1064, 1065, 1066, 107, 664, 522, 623, 186, 1067, 1068, 1069, 386, 202, 1070, 671, 393, 1071, 1072, 1073, 232, 229, 28, 476, 1074, 1075, 1076, 531, 600, 620, 1077, 597, 91, 1078, 1079, 1080, 1081, 261, 635, 1082, 612, 455, 1083, 1084, 1085, 371, 114, 126, 639, 1086, 1087, 1088, 1089, 588, 94, 634, 157, 1090, 1091, 1092, 1093, 319, 63, 37, 483, 1094, 1095, 1096, 57, 320, 1097, 672, 431, 1098, 1099, 1100, 215, 285, 471, 1101, 663, 1102, 1103, 1104, 1105, 617, 435, 462, 109, 1106, 1107, 1108, 1109, 567, 521, 667, 1110, 364, 1111, 1112, 1113, 245, 463, 625, 1114, 486, 1115, 1116, 1117, 359, 48, 228, 247, 1118, 1119, 1120, 670, 615, 605, 418, 594, 1121, 1122, 1123, 1124, 350, 122, 173, 491, 1125, 1126, 1127, 1128, 246, 577, 127, 384, 1129, 1130, 1131, 529, 653, 506, 636, 1132, 123, 1133, 1134, 1135, 166, 360, 459, 104, 1136, 1137, 1138, 293, 237, 470, 1139, 513, 1140, 1141, 1142, 33, 1143, 631, 220, 174, 1144, 1145, 1146, 404, 611, 482, 102, 1147, 1148, 1149, 373, 181, 39, 62, 1150, 1151, 1152, 627, 341, 532, 654, 142, 158, 1153, 1154, 1155, 642, 637, 501, 273, 152, 1156, 1157, 1158, 574, 118, 1159, 439, 680, 1160, 1161, 1162, 1163, 401, 589, 156, 657, 1164, 1165, 1166, 1167, 185, 630, 607, 1168, 1169, 622, 677, 1170, 1171, 1172, 297, 267, 493, 203, 196, 1173, 1174, 1175, 240, 381, 1176, 182, 137, 1177, 1178, 1179, 1180, 613, 294, 675, 1181, 282, 1182, 1183, 1184, 659, 296, 101, 385, 1185, 1186, 1187, 512, 666, 618, 277, 1188, 1189, 1190, 1191, 1192, 1193, 231, 52, 481, 179, 1194, 1195, 1196, 598, 655, 192, 619, 591, 604, 1197, 1198, 1199, 264, 337, 678, 417, 1200, 1201, 1202, 1203, 1204, 1205, 420, 480, 99, 1206, 1207, 1208, 58, 351, 429, 243, 1209, 1210, 1211, 595, 596, 204, 430, 454, 1212, 1213, 1214, 1215, 188, 629, 1216, 590, 193, 187, 1217, 1218, 1219, 1220, 599, 316, 283, 358, 1221, 1222, 1223, 365, 197, 387, 1224, 144, 1225, 1226, 1227, 284, 43, 433, 117, 1228, 1229, 1230, 369, 265, 175, 207, 1231, 1232, 1233, 406, 268, 610, 656, 201, 1234, 1235, 1236, 669, 632, 609, 167, 1237, 676, 1238, 1239, 1240, 456, 668, 160, 143, 159, 1241, 1242, 1243}));
            pso.calculate(pso.getParticle(0));
            for(final Particle particle : pso.getParticles())
            {
                particle.assignPBest();
            }
            pso.assignGBest();
            System.out.println(pso.getFitness());
            @NotNull final Observer webContent = (o, arg) ->
            {
                if(arg instanceof String)
                {
                    @NotNull final WebView result = new WebView();
                    result.getEngine().loadContent((String) arg, "text/html");
                    if(content != null)
                    {
                        content.update(null, result);
                    }
                }
            };
            Platform.runLater(() -> CMCategory.this.createResultResourceFile(webContent, pso.getGBest(), dsLoader, dbLoader));
        });
    }

    private void createResultResourceFile(final Observer callback, @NotNull final Data gBest, @NotNull final DatasetGenerator dsLoader, @NotNull final DBProblemLoader dbLoader)
    {
        @NotNull final StringBuilder sb = new StringBuilder();
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
        sb.append("");
        sb.append("/* Position the tooltip text - see examples below! */");
        sb.append("position: absolute;");
        sb.append("z-index: 1;");
        sb.append("}");
        sb.append("");
        sb.append("/* Show the tooltip text when you mouse over the tooltip container */");
        sb.append(".tooltip:hover .tooltiptext {");
        sb.append("visibility: visible;");
        sb.append("}");
        sb.append("</style>");
        sb.append("<body>");
        @NotNull final ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(() -> sb.append(this._formatLesson(gBest, dsLoader, dbLoader)));
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
        }
        sb.append("");
        sb.append("</body>");
        sb.append("</html>");

        callback.update(null, sb.toString());
    }

    @SuppressWarnings("ConstantConditions") private String _formatLesson(@NotNull final Data gBest, @NotNull final DatasetGenerator dsLoader, @NotNull final DBProblemLoader dbLoader)
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
        @NotNull final Int2ObjectMap<DBClassroom> decoded_classrooms    = dbLoader.getClassrooms();
        @NotNull final Int2ObjectMap<DBDay>       decoded_days          = dbLoader.getDays();
        @NotNull final Int2ObjectMap<DBSubject>   decoded_subjects      = dbLoader.getSubjects();
        @NotNull final Int2ObjectMap<DBLecture>   decoded_lecturers     = dbLoader.getLecturers();
        @NotNull final Int2ObjectMap<DBClass>     decoded_classes       = dbLoader.getClasses();
        @NotNull final Int2ObjectMap<DBPeriod>    decoded_period        = dbLoader.getPeriods();

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
                                tmp_query.append("<td class=\"tooltip\" colspan=\"").append(lesson_sks).append("\" style=\"color: black\">");
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

        for(final int _classroom : dbLoader.getClassrooms().keySet())
        {
            if(!query.containsKey(_classroom))
            {
                query.put(_classroom, new Int2ObjectLinkedOpenHashMap<>(arranged_day.length));
                for(final int _day : arranged_day)
                {
                    @NotNull final StringBuilder tmp_query = new StringBuilder();
                    dbLoader.getClassroom(_classroom).getTimeoff().getAvailabilities().get(_day - 1);
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
        combined.append("<table width=\"")
                .append(((decoded_period.size() * decoded_days.size() + 1) * 60) + ((decoded_days.size() - 1) * 85))
                .append("px\">");
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
}