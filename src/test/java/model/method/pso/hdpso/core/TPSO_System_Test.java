package model.method.pso.hdpso.core;

import it.unimi.dsi.fastutil.ints.IntArrays;
import java.util.Arrays;
import model.database.component.DBSchool;
import model.database.loader.DBProblemLoader;
import model.dataset.loader.DatasetGenerator;
import model.method.pso.hdpso.component.Data;
import model.method.pso.hdpso.component.Particle;
import model.method.pso.hdpso.component.Position;
import model.method.pso.hdpso.component.Setting;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.method.pso.hdpso.core> created by : 
 * Name         : syafiq
 * Date / Time  : 15 December 2016, 11:58 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings("Duplicates") public class TPSO_System_Test
{
    private DatasetGenerator dsLoader;

    @SuppressWarnings("Duplicates") @Before public void initialize()
    {
        DBSchool school = new DBSchool(1, "Program Teknologi Informasi dan Ilmu Komputer Universitas Brawijaya", "PTIIK UB", "Jl. Veteran No.8, Ketawanggede, Kec. Lowokwaru, Kota Malang, Jawa Timur", "2013 - 2014", 0, 17, 5);
        Assert.assertNotNull(school);

        DBProblemLoader dbLoader = new DBProblemLoader(school);
        Assert.assertNotNull(dbLoader);

        dbLoader.loadData();

        this.dsLoader = new DatasetGenerator(dbLoader);
        Assert.assertNotNull(dsLoader);
    }

    @Test public void testSystem()
    {
        Setting setting = Setting.getInstance();
        setting.max_particle = 50;
        setting.max_epoch = 1000;
        setting.bLoc_min = 0.600;
        setting.bLoc_max = 0.900;
        setting.bGlob_min = 0.100;
        setting.bGlob_max = 0.400;
        setting.bRand_min = 0.001;
        setting.bRand_max = 0.100;
        setting.total_core = 4;

        @NotNull final PSO pso = new PSO(this.dsLoader);
        Assert.assertNotNull(pso);
        pso.initialize();
        while(!pso.isConditionSatisfied())
        {
            for(@NotNull final Particle particle : pso.getParticles())
            {
                particle.assignPBest();
            }
            pso.assignGBest();
            for(@NotNull final Particle particle : pso.getParticles())
            {
                particle.calculateVelocity(pso.getGBest(), pso.getEpoch(), setting.max_epoch);
                particle.updateData();
                pso.repair(particle);
                pso.calculate(particle);
            }
            pso.updateStoppingCondition();
        }
        int counter = -1;
        for(@NotNull final Particle particle : pso.getParticles())
        {
            System.out.printf("Particle %d %f \n", ++counter, particle.getFitness());
            for(int i = -1, is = particle.getData().getPositionSize(); ++i < is; )
            {
                System.out.printf("%b\t%s\n", Arrays.equals(particle.getData().getPosition(i).getPosition(), particle.getPBest().getPosition(i).getPosition()), Arrays.toString(particle.getData().getPosition(i).getPosition()));
                System.out.printf("%b\t%s\n", Arrays.equals(particle.getPBest().getPosition(i).getPosition(), particle.getData().getPosition(i).getPosition()), Arrays.toString(particle.getPBest().getPosition(i).getPosition()));
            }
        }
        System.out.printf("GBest %f\n", pso.getFitness());
        final Data gBest = pso.getGBest();
        for(int i = -1, is = gBest.getPositionSize(); ++i < is; )
        {
            System.out.println(Arrays.toString(gBest.getPosition(i).getPosition()));
        }
    }

    @Test public void testSoftwareFitness()
    {
        Setting setting = Setting.getInstance();
        setting.max_particle = 1;
        setting.max_epoch = 1000;
        setting.bLoc_min = 0.600;
        setting.bLoc_max = 0.900;
        setting.bGlob_min = 0.100;
        setting.bGlob_max = 0.400;
        setting.bRand_min = 0.001;
        setting.bRand_max = 0.100;
        setting.total_core = 4;

        @NotNull final PSO pso = new PSO(this.dsLoader);
        Assert.assertNotNull(pso);
        pso.initialize();
        Position.replace(pso.getParticle(0).getData().getPosition(0), new Position(new int[] {691, 692, 693, 694, 695, 696, 697, 698, 699, 700, 701, 702, 703, 704, 705, 706, 707, 708, 709, 710, 711, 712, 713, 714, 715, 716, 717, 718, 719, 720, 721, 722, 723, 724, 725, 726, 727, 728, 729, 730, 731, 732, 733, 734, 735, 256, 251, 253, 258, 252, 250, 736, 737, 738, 254, 739, 249, 740, 255, 257, 741, 742, 743, 744}));
        Position.replace(pso.getParticle(0).getData().getPosition(1), new Position(new int[] {545, 779, 547, 780, 548, 583, 546, 781, 782, 783, 582, 649, 647, 586, 549, 587, 784, 785, 786, 648, 685, 643, 645, 584, 650, 787, 788, 789, 689, 686, 690, 790, 651, 585, 791, 792, 793, 794, 795, 688, 644, 687, 646, 652, 796, 797, 798}));
        Position.replace(pso.getParticle(0).getData().getPosition(2), new Position(new int[] {799, 579, 578, 800, 801, 802, 803, 804, 805, 806, 807, 808, 809, 810, 811, 812, 813, 581, 814, 815, 816, 817, 818, 819, 820, 821, 822, 682, 823, 681, 824, 825, 826, 827, 828, 829, 830, 831, 832, 833, 580, 834, 684, 835, 836, 837, 838, 839, 840, 841, 842, 843, 844, 683, 845, 846, 847, 848, 849, 850, 851, 852, 853, 854, 855, 856}));
        Position.replace(pso.getParticle(0).getData().getPosition(3), new Position(new int[] {305, 301, 313, 7, 304, 12, 745, 746, 747, 310, 309, 303, 17, 748, 749, 21, 750, 751, 752, 302, 299, 306, 6, 69, 75, 753, 754, 755, 308, 312, 311, 23, 1, 2, 756, 757, 758, 759, 307, 300, 10, 25, 70, 760, 761, 762, 79, 20, 82, 4, 83, 26, 763, 764, 765, 24, 15, 5, 22, 16, 81, 766, 767, 768, 19, 74, 11, 8, 72, 0, 769, 770, 771, 14, 77, 80, 78, 13, 76, 772, 773, 774, 9, 775, 18, 71, 73, 3, 776, 777, 778}));
        Position.replace(pso.getParticle(0).getData().getPosition(4), new Position(new int[] {447, 857, 356, 467, 278, 367, 446, 180, 218, 85, 205, 477, 96, 84, 234, 565, 141, 858, 859, 860, 329, 861, 391, 409, 562, 378, 862, 559, 568, 555, 413, 169, 221, 177, 98, 379, 516, 183, 863, 864, 865, 866, 168, 542, 867, 441, 86, 658, 119, 445, 131, 474, 412, 270, 375, 222, 209, 519, 868, 151, 208, 139, 869, 217, 289, 263, 870, 146, 172, 394, 541, 395, 95, 382, 374, 133, 444, 334, 54, 465, 132, 67, 224, 437, 110, 332, 452, 426, 396, 103, 871, 872, 370, 93, 295, 327, 526, 87, 873, 130, 50, 330, 443, 51, 275, 68, 488, 326, 389, 874, 490, 348, 875, 539, 368, 561, 97, 876, 274, 153, 451, 427, 210, 60, 505, 164, 558, 349, 877, 55, 219, 434, 324, 41, 408, 489, 402, 510, 466, 453, 450, 262, 440, 640, 878, 279, 226, 411, 64, 879, 354, 112, 421, 40, 340, 400, 178, 880, 554, 550, 388, 399, 442, 405, 223, 556, 557, 140, 881, 271, 343, 882, 525, 281, 155, 383, 504, 432, 883, 66, 362, 884, 149, 478, 363, 660, 397, 407, 292, 331, 534, 885, 206, 571, 572, 624, 614, 49, 886, 887, 328, 515, 508, 888, 259, 674, 355, 606, 540, 380, 889, 570, 392, 553, 280, 890, 575, 891, 214, 502, 535, 150, 892, 893, 894, 424, 286, 469, 524, 895, 896, 897, 898, 236, 233, 460, 487, 899, 900, 901, 211, 59, 105, 485, 902, 903, 904, 30, 633, 366, 61, 905, 906, 907, 496, 530, 528, 436, 908, 909, 910, 911, 912, 913, 238, 914, 628, 626, 161, 495, 915, 916, 917, 339, 288, 136, 176, 918, 919, 920, 269, 344, 116, 335, 921, 922, 923, 376, 551, 42, 372, 924, 925, 926, 603, 536, 544, 523, 927, 928, 929, 930, 931, 932, 933, 576, 321, 934, 135, 225, 935, 936, 937, 357, 419, 938, 242, 336, 939, 940, 941, 200, 322, 942, 212, 415, 943, 944, 945, 100, 347, 552, 361, 946, 947, 948, 949, 113, 32, 134, 475, 950, 951, 952, 287, 235, 194, 138, 191, 953, 954, 955, 35, 338, 108, 410, 956, 957, 958, 184, 377, 272, 106, 959, 960, 961, 464, 507, 198, 145, 148, 962, 963, 964, 499, 323, 965, 966, 967, 533, 497, 968, 969, 970, 342, 527, 500, 27, 298, 971, 972, 973, 352, 154, 315, 403, 974, 975, 976, 673, 608, 503, 195, 162, 147, 977, 978, 979, 662, 472, 90, 560, 980, 981, 982, 983, 46, 325, 458, 492, 984, 985, 986, 987, 31, 111, 621, 47, 988, 989, 990, 991, 88, 36, 428, 511, 992, 993, 994, 995, 213, 425, 244, 390, 996, 997, 998, 593, 345, 999, 92, 566, 1000, 1001, 1002, 34, 124, 423, 422, 1003, 1004, 1005, 216, 129, 266, 29, 1006, 1007, 1008, 494, 601, 509, 573, 448, 1009, 1010, 1011, 1012, 230, 290, 189, 543, 564, 1013, 1014, 1015, 121, 563, 416, 165, 1016, 1017, 1018, 227, 473, 1019, 170, 514, 1020, 1021, 1022, 318, 314, 449, 44, 1023, 1024, 1025, 276, 353, 115, 65, 1026, 1027, 1028, 239, 479, 89, 163, 1029, 1030, 1031, 1032, 518, 398, 661, 537, 190, 1033, 1034, 1035, 346, 461, 438, 468, 1036, 1037, 1038, 56, 457, 53, 260, 1039, 1040, 1041, 199, 538, 616, 679, 171, 1042, 1043, 1044, 1045, 592, 125, 1046, 517, 1047, 45, 1048, 1049, 1050, 291, 569, 638, 665, 520, 1051, 1052, 1053, 641, 1054, 128, 414, 38, 1055, 1056, 1057, 241, 1058, 602, 317, 484, 1059, 1060, 1061, 333, 248, 498, 120, 1062, 1063, 1064, 1065, 1066, 107, 664, 522, 623, 186, 1067, 1068, 1069, 386, 202, 1070, 671, 393, 1071, 1072, 1073, 232, 229, 28, 476, 1074, 1075, 1076, 531, 600, 620, 1077, 597, 91, 1078, 1079, 1080, 1081, 261, 635, 1082, 612, 455, 1083, 1084, 1085, 371, 114, 126, 639, 1086, 1087, 1088, 1089, 588, 94, 634, 157, 1090, 1091, 1092, 1093, 319, 63, 37, 483, 1094, 1095, 1096, 57, 320, 1097, 672, 431, 1098, 1099, 1100, 215, 285, 471, 1101, 663, 1102, 1103, 1104, 1105, 617, 435, 462, 109, 1106, 1107, 1108, 1109, 567, 521, 667, 1110, 364, 1111, 1112, 1113, 245, 463, 625, 1114, 486, 1115, 1116, 1117, 359, 48, 228, 247, 1118, 1119, 1120, 670, 615, 605, 418, 594, 1121, 1122, 1123, 1124, 350, 122, 173, 491, 1125, 1126, 1127, 1128, 246, 577, 127, 384, 1129, 1130, 1131, 529, 653, 506, 636, 1132, 123, 1133, 1134, 1135, 166, 360, 459, 104, 1136, 1137, 1138, 293, 237, 470, 1139, 513, 1140, 1141, 1142, 33, 1143, 631, 220, 174, 1144, 1145, 1146, 404, 611, 482, 102, 1147, 1148, 1149, 373, 181, 39, 62, 1150, 1151, 1152, 627, 341, 532, 654, 142, 158, 1153, 1154, 1155, 642, 637, 501, 273, 152, 1156, 1157, 1158, 574, 118, 1159, 439, 680, 1160, 1161, 1162, 1163, 401, 589, 156, 657, 1164, 1165, 1166, 1167, 185, 630, 607, 1168, 1169, 622, 677, 1170, 1171, 1172, 297, 267, 493, 203, 196, 1173, 1174, 1175, 240, 381, 1176, 182, 137, 1177, 1178, 1179, 1180, 613, 294, 675, 1181, 282, 1182, 1183, 1184, 659, 296, 101, 385, 1185, 1186, 1187, 512, 666, 618, 277, 1188, 1189, 1190, 1191, 1192, 1193, 231, 52, 481, 179, 1194, 1195, 1196, 598, 655, 192, 619, 591, 604, 1197, 1198, 1199, 264, 337, 678, 417, 1200, 1201, 1202, 1203, 1204, 1205, 420, 480, 99, 1206, 1207, 1208, 58, 351, 429, 243, 1209, 1210, 1211, 595, 596, 204, 430, 454, 1212, 1213, 1214, 1215, 188, 629, 1216, 590, 193, 187, 1217, 1218, 1219, 1220, 599, 316, 283, 358, 1221, 1222, 1223, 365, 197, 387, 1224, 144, 1225, 1226, 1227, 284, 43, 433, 117, 1228, 1229, 1230, 369, 265, 175, 207, 1231, 1232, 1233, 406, 268, 610, 656, 201, 1234, 1235, 1236, 669, 632, 609, 167, 1237, 676, 1238, 1239, 1240, 456, 668, 160, 143, 159, 1241, 1242, 1243}));
        pso.calculate(pso.getParticle(0));
        System.out.println(pso.getParticle(0).getFitness());
        pso.calculate(pso.getParticle(0));
        System.out.println(pso.getParticle(0).getFitness());
        pso.calculate(pso.getParticle(0));
        System.out.println(pso.getParticle(0).getFitness());
        pso.calculate(pso.getParticle(0));
        System.out.println(pso.getParticle(0).getFitness());
        pso.calculate(pso.getParticle(0));
        System.out.println(pso.getParticle(0).getFitness());
        pso.calculate(pso.getParticle(0));
        System.out.println(pso.getParticle(0).getFitness());

        final Data data = pso.getParticle(0).getData();
        for(int i = -1, is = data.getPositionSize(); ++i < is; )
        {
            System.out.println(Arrays.toString(data.getPosition(i).getPosition()));
            //IntArrays.quickSort(data.getPosition(i).getPosition());
            //System.out.println(Arrays.toString(data.getPosition(i).getPosition()));
        }
    }

    @Test
    public void testSoftwareOrdering()
    {
        int a[][]   = new int[5][];
        int order[] = new int[] {2, 0, 1, 3, 4};
        a[0] = new int[] {546, 0, 548, 0, 549, 584, 547, 0, 0, 0, 583, 650, 648, 587, 550, 588, 0, 0, 0, 649, 686, 644, 646, 585, 651, 0, 0, 0, 690, 687, 691, 0, 652, 586, 0, 0, 0, 0, 0, 689, 645, 688, 647, 653, 0, 0, 0};
        a[1] = new int[] {0, 580, 579, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 582, 0, 0, 0, 0, 0, 0, 0, 0, 0, 683, 0, 682, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 581, 0, 685, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 684, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        a[2] = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 257, 252, 254, 259, 253, 251, 0, 0, 0, 255, 0, 250, 0, 256, 258, 0, 0, 0, 0};
        a[3] = new int[] {306, 302, 314, 8, 305, 13, 0, 0, 0, 311, 310, 304, 18, 0, 0, 22, 0, 0, 0, 303, 300, 307, 7, 70, 76, 0, 0, 0, 309, 313, 312, 24, 2, 3, 0, 0, 0, 0, 308, 301, 11, 26, 71, 0, 0, 0, 80, 21, 83, 5, 84, 27, 0, 0, 0, 25, 16, 6, 23, 17, 82, 0, 0, 0, 20, 75, 12, 9, 73, 1, 0, 0, 0, 15, 78, 81, 79, 14, 77, 0, 0, 0, 10, 0, 19, 72, 74, 4, 0, 0, 0};
        a[4] = new int[] {448, 0, 357, 468, 279, 368, 447, 181, 219, 86, 206, 478, 97, 85, 235, 566, 142, 0, 0, 0, 330, 0, 392, 410, 563, 379, 0, 560, 569, 556, 414, 170, 222, 178, 99, 380, 517, 184, 0, 0, 0, 0, 169, 543, 0, 442, 87, 659, 120, 446, 132, 475, 413, 271, 376, 223, 210, 520, 0, 152, 209, 140, 0, 218, 290, 264, 0, 147, 173, 395, 542, 396, 96, 383, 375, 134, 445, 335, 55, 466, 133, 68, 225, 438, 111, 333, 453, 427, 397, 104, 0, 0, 371, 94, 296, 328, 527, 88, 0, 131, 51, 331, 444, 52, 276, 69, 489, 327, 390, 0, 491, 349, 0, 540, 369, 562, 98, 0, 275, 154, 452, 428, 211, 61, 506, 165, 559, 350, 0, 56, 220, 435, 325, 42, 409, 490, 403, 511, 467, 454, 451, 263, 441, 641, 0, 280, 227, 412, 65, 0, 355, 113, 422, 41, 341, 401, 179, 0, 555, 551, 389, 400, 443, 406, 224, 557, 558, 141, 0, 272, 344, 0, 526, 282, 156, 384, 505, 433, 0, 67, 363, 0, 150, 479, 364, 661, 398, 408, 293, 332, 535, 0, 207, 572, 573, 625, 615, 50, 0, 0, 329, 516, 509, 0, 260, 675, 356, 607, 541, 381, 0, 571, 393, 554, 281, 0, 576, 0, 215, 503, 536, 151, 0, 0, 0, 425, 287, 470, 525, 0, 0, 0, 0, 237, 234, 461, 488, 0, 0, 0, 212, 60, 106, 486, 0, 0, 0, 31, 634, 367, 62, 0, 0, 0, 497, 531, 529, 437, 0, 0, 0, 0, 0, 0, 239, 0, 629, 627, 162, 496, 0, 0, 0, 340, 289, 137, 177, 0, 0, 0, 270, 345, 117, 336, 0, 0, 0, 377, 552, 43, 373, 0, 0, 0, 604, 537, 545, 524, 0, 0, 0, 0, 0, 0, 0, 577, 322, 0, 136, 226, 0, 0, 0, 358, 420, 0, 243, 337, 0, 0, 0, 201, 323, 0, 213, 416, 0, 0, 0, 101, 348, 553, 362, 0, 0, 0, 0, 114, 33, 135, 476, 0, 0, 0, 288, 236, 195, 139, 192, 0, 0, 0, 36, 339, 109, 411, 0, 0, 0, 185, 378, 273, 107, 0, 0, 0, 465, 508, 199, 146, 149, 0, 0, 0, 500, 324, 0, 0, 0, 534, 498, 0, 0, 0, 343, 528, 501, 28, 299, 0, 0, 0, 353, 155, 316, 404, 0, 0, 0, 674, 609, 504, 196, 163, 148, 0, 0, 0, 663, 473, 91, 561, 0, 0, 0, 0, 47, 326, 459, 493, 0, 0, 0, 0, 32, 112, 622, 48, 0, 0, 0, 0, 89, 37, 429, 512, 0, 0, 0, 0, 214, 426, 245, 391, 0, 0, 0, 594, 346, 0, 93, 567, 0, 0, 0, 35, 125, 424, 423, 0, 0, 0, 217, 130, 267, 30, 0, 0, 0, 495, 602, 510, 574, 449, 0, 0, 0, 0, 231, 291, 190, 544, 565, 0, 0, 0, 122, 564, 417, 166, 0, 0, 0, 228, 474, 0, 171, 515, 0, 0, 0, 319, 315, 450, 45, 0, 0, 0, 277, 354, 116, 66, 0, 0, 0, 240, 480, 90, 164, 0, 0, 0, 0, 519, 399, 662, 538, 191, 0, 0, 0, 347, 462, 439, 469, 0, 0, 0, 57, 458, 54, 261, 0, 0, 0, 200, 539, 617, 680, 172, 0, 0, 0, 0, 593, 126, 0, 518, 0, 46, 0, 0, 0, 292, 570, 639, 666, 521, 0, 0, 0, 642, 0, 129, 415, 39, 0, 0, 0, 242, 0, 603, 318, 485, 0, 0, 0, 334, 249, 499, 121, 0, 0, 0, 0, 0, 108, 665, 523, 624, 187, 0, 0, 0, 387, 203, 0, 672, 394, 0, 0, 0, 233, 230, 29, 477, 0, 0, 0, 532, 601, 621, 0, 598, 92, 0, 0, 0, 0, 262, 636, 0, 613, 456, 0, 0, 0, 372, 115, 127, 640, 0, 0, 0, 0, 589, 95, 635, 158, 0, 0, 0, 0, 320, 64, 38, 484, 0, 0, 0, 58, 321, 0, 673, 432, 0, 0, 0, 216, 286, 472, 0, 664, 0, 0, 0, 0, 618, 436, 463, 110, 0, 0, 0, 0, 568, 522, 668, 0, 365, 0, 0, 0, 246, 464, 626, 0, 487, 0, 0, 0, 360, 49, 229, 248, 0, 0, 0, 671, 616, 606, 419, 595, 0, 0, 0, 0, 351, 123, 174, 492, 0, 0, 0, 0, 247, 578, 128, 385, 0, 0, 0, 530, 654, 507, 637, 0, 124, 0, 0, 0, 167, 361, 460, 105, 0, 0, 0, 294, 238, 471, 0, 514, 0, 0, 0, 34, 0, 632, 221, 175, 0, 0, 0, 405, 612, 483, 103, 0, 0, 0, 374, 182, 40, 63, 0, 0, 0, 628, 342, 533, 655, 143, 159, 0, 0, 0, 643, 638, 502, 274, 153, 0, 0, 0, 575, 119, 0, 440, 681, 0, 0, 0, 0, 402, 590, 157, 658, 0, 0, 0, 0, 186, 631, 608, 0, 0, 623, 678, 0, 0, 0, 298, 268, 494, 204, 197, 0, 0, 0, 241, 382, 0, 183, 138, 0, 0, 0, 0, 614, 295, 676, 0, 283, 0, 0, 0, 660, 297, 102, 386, 0, 0, 0, 513, 667, 619, 278, 0, 0, 0, 0, 0, 0, 232, 53, 482, 180, 0, 0, 0, 599, 656, 193, 620, 592, 605, 0, 0, 0, 265, 338, 679, 418, 0, 0, 0, 0, 0, 0, 421, 481, 100, 0, 0, 0, 59, 352, 430, 244, 0, 0, 0, 596, 597, 205, 431, 455, 0, 0, 0, 0, 189, 630, 0, 591, 194, 188, 0, 0, 0, 0, 600, 317, 284, 359, 0, 0, 0, 366, 198, 388, 0, 145, 0, 0, 0, 285, 44, 434, 118, 0, 0, 0, 370, 266, 176, 208, 0, 0, 0, 407, 269, 611, 657, 202, 0, 0, 0, 670, 633, 610, 168, 0, 677, 0, 0, 0, 457, 669, 161, 144, 160, 0, 0, 0};
        for(int i : order)
        {
            System.out.println(i);
            System.out.println(Arrays.toString(a[i]));
            IntArrays.quickSort(a[i]);
            System.out.println(Arrays.toString(a[i]));
        }
    }

    @Test
    public void testSoftwareOrderAdapt()
    {
        int a[][]   = new int[5][];
        int order[] = new int[] {2, 0, 1, 3, 4};
        a[0] = new int[] {546, 0, 548, 0, 549, 584, 547, 0, 0, 0, 583, 650, 648, 587, 550, 588, 0, 0, 0, 649, 686, 644, 646, 585, 651, 0, 0, 0, 690, 687, 691, 0, 652, 586, 0, 0, 0, 0, 0, 689, 645, 688, 647, 653, 0, 0, 0};
        a[1] = new int[] {0, 580, 579, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 582, 0, 0, 0, 0, 0, 0, 0, 0, 0, 683, 0, 682, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 581, 0, 685, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 684, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        a[2] = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 257, 252, 254, 259, 253, 251, 0, 0, 0, 255, 0, 250, 0, 256, 258, 0, 0, 0, 0};
        a[3] = new int[] {306, 302, 314, 8, 305, 13, 0, 0, 0, 311, 310, 304, 18, 0, 0, 22, 0, 0, 0, 303, 300, 307, 7, 70, 76, 0, 0, 0, 309, 313, 312, 24, 2, 3, 0, 0, 0, 0, 308, 301, 11, 26, 71, 0, 0, 0, 80, 21, 83, 5, 84, 27, 0, 0, 0, 25, 16, 6, 23, 17, 82, 0, 0, 0, 20, 75, 12, 9, 73, 1, 0, 0, 0, 15, 78, 81, 79, 14, 77, 0, 0, 0, 10, 0, 19, 72, 74, 4, 0, 0, 0};
        a[4] = new int[] {448, 0, 357, 468, 279, 368, 447, 181, 219, 86, 206, 478, 97, 85, 235, 566, 142, 0, 0, 0, 330, 0, 392, 410, 563, 379, 0, 560, 569, 556, 414, 170, 222, 178, 99, 380, 517, 184, 0, 0, 0, 0, 169, 543, 0, 442, 87, 659, 120, 446, 132, 475, 413, 271, 376, 223, 210, 520, 0, 152, 209, 140, 0, 218, 290, 264, 0, 147, 173, 395, 542, 396, 96, 383, 375, 134, 445, 335, 55, 466, 133, 68, 225, 438, 111, 333, 453, 427, 397, 104, 0, 0, 371, 94, 296, 328, 527, 88, 0, 131, 51, 331, 444, 52, 276, 69, 489, 327, 390, 0, 491, 349, 0, 540, 369, 562, 98, 0, 275, 154, 452, 428, 211, 61, 506, 165, 559, 350, 0, 56, 220, 435, 325, 42, 409, 490, 403, 511, 467, 454, 451, 263, 441, 641, 0, 280, 227, 412, 65, 0, 355, 113, 422, 41, 341, 401, 179, 0, 555, 551, 389, 400, 443, 406, 224, 557, 558, 141, 0, 272, 344, 0, 526, 282, 156, 384, 505, 433, 0, 67, 363, 0, 150, 479, 364, 661, 398, 408, 293, 332, 535, 0, 207, 572, 573, 625, 615, 50, 0, 0, 329, 516, 509, 0, 260, 675, 356, 607, 541, 381, 0, 571, 393, 554, 281, 0, 576, 0, 215, 503, 536, 151, 0, 0, 0, 425, 287, 470, 525, 0, 0, 0, 0, 237, 234, 461, 488, 0, 0, 0, 212, 60, 106, 486, 0, 0, 0, 31, 634, 367, 62, 0, 0, 0, 497, 531, 529, 437, 0, 0, 0, 0, 0, 0, 239, 0, 629, 627, 162, 496, 0, 0, 0, 340, 289, 137, 177, 0, 0, 0, 270, 345, 117, 336, 0, 0, 0, 377, 552, 43, 373, 0, 0, 0, 604, 537, 545, 524, 0, 0, 0, 0, 0, 0, 0, 577, 322, 0, 136, 226, 0, 0, 0, 358, 420, 0, 243, 337, 0, 0, 0, 201, 323, 0, 213, 416, 0, 0, 0, 101, 348, 553, 362, 0, 0, 0, 0, 114, 33, 135, 476, 0, 0, 0, 288, 236, 195, 139, 192, 0, 0, 0, 36, 339, 109, 411, 0, 0, 0, 185, 378, 273, 107, 0, 0, 0, 465, 508, 199, 146, 149, 0, 0, 0, 500, 324, 0, 0, 0, 534, 498, 0, 0, 0, 343, 528, 501, 28, 299, 0, 0, 0, 353, 155, 316, 404, 0, 0, 0, 674, 609, 504, 196, 163, 148, 0, 0, 0, 663, 473, 91, 561, 0, 0, 0, 0, 47, 326, 459, 493, 0, 0, 0, 0, 32, 112, 622, 48, 0, 0, 0, 0, 89, 37, 429, 512, 0, 0, 0, 0, 214, 426, 245, 391, 0, 0, 0, 594, 346, 0, 93, 567, 0, 0, 0, 35, 125, 424, 423, 0, 0, 0, 217, 130, 267, 30, 0, 0, 0, 495, 602, 510, 574, 449, 0, 0, 0, 0, 231, 291, 190, 544, 565, 0, 0, 0, 122, 564, 417, 166, 0, 0, 0, 228, 474, 0, 171, 515, 0, 0, 0, 319, 315, 450, 45, 0, 0, 0, 277, 354, 116, 66, 0, 0, 0, 240, 480, 90, 164, 0, 0, 0, 0, 519, 399, 662, 538, 191, 0, 0, 0, 347, 462, 439, 469, 0, 0, 0, 57, 458, 54, 261, 0, 0, 0, 200, 539, 617, 680, 172, 0, 0, 0, 0, 593, 126, 0, 518, 0, 46, 0, 0, 0, 292, 570, 639, 666, 521, 0, 0, 0, 642, 0, 129, 415, 39, 0, 0, 0, 242, 0, 603, 318, 485, 0, 0, 0, 334, 249, 499, 121, 0, 0, 0, 0, 0, 108, 665, 523, 624, 187, 0, 0, 0, 387, 203, 0, 672, 394, 0, 0, 0, 233, 230, 29, 477, 0, 0, 0, 532, 601, 621, 0, 598, 92, 0, 0, 0, 0, 262, 636, 0, 613, 456, 0, 0, 0, 372, 115, 127, 640, 0, 0, 0, 0, 589, 95, 635, 158, 0, 0, 0, 0, 320, 64, 38, 484, 0, 0, 0, 58, 321, 0, 673, 432, 0, 0, 0, 216, 286, 472, 0, 664, 0, 0, 0, 0, 618, 436, 463, 110, 0, 0, 0, 0, 568, 522, 668, 0, 365, 0, 0, 0, 246, 464, 626, 0, 487, 0, 0, 0, 360, 49, 229, 248, 0, 0, 0, 671, 616, 606, 419, 595, 0, 0, 0, 0, 351, 123, 174, 492, 0, 0, 0, 0, 247, 578, 128, 385, 0, 0, 0, 530, 654, 507, 637, 0, 124, 0, 0, 0, 167, 361, 460, 105, 0, 0, 0, 294, 238, 471, 0, 514, 0, 0, 0, 34, 0, 632, 221, 175, 0, 0, 0, 405, 612, 483, 103, 0, 0, 0, 374, 182, 40, 63, 0, 0, 0, 628, 342, 533, 655, 143, 159, 0, 0, 0, 643, 638, 502, 274, 153, 0, 0, 0, 575, 119, 0, 440, 681, 0, 0, 0, 0, 402, 590, 157, 658, 0, 0, 0, 0, 186, 631, 608, 0, 0, 623, 678, 0, 0, 0, 298, 268, 494, 204, 197, 0, 0, 0, 241, 382, 0, 183, 138, 0, 0, 0, 0, 614, 295, 676, 0, 283, 0, 0, 0, 660, 297, 102, 386, 0, 0, 0, 513, 667, 619, 278, 0, 0, 0, 0, 0, 0, 232, 53, 482, 180, 0, 0, 0, 599, 656, 193, 620, 592, 605, 0, 0, 0, 265, 338, 679, 418, 0, 0, 0, 0, 0, 0, 421, 481, 100, 0, 0, 0, 59, 352, 430, 244, 0, 0, 0, 596, 597, 205, 431, 455, 0, 0, 0, 0, 189, 630, 0, 591, 194, 188, 0, 0, 0, 0, 600, 317, 284, 359, 0, 0, 0, 366, 198, 388, 0, 145, 0, 0, 0, 285, 44, 434, 118, 0, 0, 0, 370, 266, 176, 208, 0, 0, 0, 407, 269, 611, 657, 202, 0, 0, 0, 670, 633, 610, 168, 0, 677, 0, 0, 0, 457, 669, 161, 144, 160, 0, 0, 0};

        int ci    = -1;
        int i     = order[++ci];
        int _null = 690;
        for(int j = -1, js = a[i].length; ++j < js; )
        {
            if(a[i][j] == 0)
            {
                a[i][j] = ++_null;
            }
            else
            {
                --a[i][j];
            }
        }
        i = order[++ci];
        _null = 778;
        for(int j = -1, js = a[i].length; ++j < js; )
        {
            if(a[i][j] == 0)
            {
                a[i][j] = ++_null;
            }
            else
            {
                --a[i][j];
            }
        }
        i = order[++ci];
        _null = 798;
        for(int j = -1, js = a[i].length; ++j < js; )
        {
            if(a[i][j] == 0)
            {
                a[i][j] = ++_null;
            }
            else
            {
                --a[i][j];
            }
        }
        i = order[++ci];
        _null = 744;
        for(int j = -1, js = a[i].length; ++j < js; )
        {
            if(a[i][j] == 0)
            {
                a[i][j] = ++_null;
            }
            else
            {
                --a[i][j];
            }
        }
        i = order[++ci];
        _null = 856;
        for(int j = -1, js = a[i].length; ++j < js; )
        {
            if(a[i][j] == 0)
            {
                a[i][j] = ++_null;
            }
            else
            {
                --a[i][j];
            }
        }

        for(int k : order)
        {
            System.out.println(Arrays.toString(a[k]));
            //IntArrays.quickSort(a[k]);
            //System.out.println(Arrays.toString(a[k]));
        }

    }
}
