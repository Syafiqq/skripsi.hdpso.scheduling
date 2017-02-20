package model.database.component;

import model.database.component.metadata.DBMSchool;
import model.util.Converter;
import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.database.component> created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 28 January 2017, 8:46 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings("unused") public class DBConstraint
{
    private final int       id;
    private final DBMSchool school;
    private       double    subject;
    private       double    lecture;
    private       double    klass;
    private       double    classroom;
    private       double    lPlacement;
    private       double    cPlacement;
    private       double    link;
    private       double    allow;
    private       boolean   isSubject;
    private       boolean   isLecture;
    private       boolean   isKlass;
    private       boolean   isClassroom;
    private       boolean   isLPlacement;
    private       boolean   isCPlacement;
    private       boolean   isLink;
    private       boolean   isAllow;

    public DBConstraint(int id, DBMSchool school, double subject, double lecture, double klass, double classroom, double lPlacement, double cPlacement, double link, double allow, boolean isSubject, boolean isLecture, boolean isKlass, boolean isClassroom, boolean isLPlacement, boolean isCPlacement, boolean isLink, boolean isAllow)
    {
        this.id = id;
        this.school = school;
        this.subject = subject;
        this.lecture = lecture;
        this.klass = klass;
        this.classroom = classroom;
        this.lPlacement = lPlacement;
        this.cPlacement = cPlacement;
        this.link = link;
        this.allow = allow;
        this.isSubject = isSubject;
        this.isLecture = isLecture;
        this.isKlass = isKlass;
        this.isClassroom = isClassroom;
        this.isLPlacement = isLPlacement;
        this.isCPlacement = isCPlacement;
        this.isLink = isLink;
        this.isAllow = isAllow;
    }

    @NotNull public static CompiledConstraint generateCompiledConstraint(@NotNull final DBConstraint constraint)
    {
        return new CompiledConstraint(constraint);
    }

    public int getId()
    {
        return this.id;
    }

    public DBMSchool getSchool()
    {
        return this.school;
    }

    public double getSubject()
    {
        return this.subject;
    }

    public void setSubject(double subject)
    {
        this.subject = subject;
    }

    public double getLecture()
    {
        return this.lecture;
    }

    public void setLecture(double lecture)
    {
        this.lecture = lecture;
    }

    public double getKlass()
    {
        return this.klass;
    }

    public void setKlass(double klass)
    {
        this.klass = klass;
    }

    public double getClassroom()
    {
        return this.classroom;
    }

    public void setClassroom(double classroom)
    {
        this.classroom = classroom;
    }

    public double getLPlacement()
    {
        return this.lPlacement;
    }

    public void setLPlacement(double lPlacement)
    {
        this.lPlacement = lPlacement;
    }

    public double getCPlacement()
    {
        return this.cPlacement;
    }

    public void setCPlacement(double cPlacement)
    {
        this.cPlacement = cPlacement;
    }

    public double getLink()
    {
        return this.link;
    }

    public void setLink(double link)
    {
        this.link = link;
    }

    public double getAllow()
    {
        return this.allow;
    }

    public void setAllow(double allow)
    {
        this.allow = allow;
    }

    public boolean isSubject()
    {
        return this.isSubject;
    }

    public void setSubject(boolean subject)
    {
        this.isSubject = subject;
    }

    public boolean isLecture()
    {
        return this.isLecture;
    }

    public void setLecture(boolean lecture)
    {
        this.isLecture = lecture;
    }

    public boolean isKlass()
    {
        return this.isKlass;
    }

    public void setKlass(boolean klass)
    {
        this.isKlass = klass;
    }

    public boolean isClassroom()
    {
        return this.isClassroom;
    }

    public void setClassroom(boolean classroom)
    {
        this.isClassroom = classroom;
    }

    public boolean isLPlacement()
    {
        return this.isLPlacement;
    }

    public void setLPlacement(boolean LPlacement)
    {
        this.isLPlacement = LPlacement;
    }

    public boolean isCPlacement()
    {
        return this.isCPlacement;
    }

    public void setCPlacement(boolean CPlacement)
    {
        this.isCPlacement = CPlacement;
    }

    public boolean isLink()
    {
        return this.isLink;
    }

    public void setLink(boolean link)
    {
        this.isLink = link;
    }

    public boolean isAllow()
    {
        return this.isAllow;
    }

    public void setAllow(boolean allow)
    {
        this.isAllow = allow;
    }

    public static class CompiledConstraint
    {
        final private double subject;
        final private double lecture;
        final private double klass;
        final private double classroom;
        final private double lPlacement;
        final private double cPlacement;
        final private double link;
        final private double allow;

        CompiledConstraint(@NotNull final DBConstraint constraint)
        {
            this.subject = Converter.integerToBooleanInteger(constraint.isSubject()) * constraint.getSubject();
            this.lecture = Converter.integerToBooleanInteger(constraint.isLecture()) * constraint.getLecture();
            this.klass = Converter.integerToBooleanInteger(constraint.isKlass()) * constraint.getKlass();
            this.classroom = Converter.integerToBooleanInteger(constraint.isClassroom()) * constraint.getClassroom();
            this.lPlacement = Converter.integerToBooleanInteger(constraint.isLPlacement()) * constraint.getLPlacement();
            this.cPlacement = Converter.integerToBooleanInteger(constraint.isCPlacement()) * constraint.getCPlacement();
            this.link = Converter.integerToBooleanInteger(constraint.isLink()) * constraint.getLink();
            this.allow = Converter.integerToBooleanInteger(constraint.isAllow()) * constraint.getAllow();
        }

        public double getSubject()
        {
            return this.subject;
        }

        public double getLecture()
        {
            return this.lecture;
        }

        public double getKlass()
        {
            return this.klass;
        }

        public double getClassroom()
        {
            return this.classroom;
        }

        public double getLPlacement()
        {
            return this.lPlacement;
        }

        public double getCPlacement()
        {
            return this.cPlacement;
        }

        public double getLink()
        {
            return this.link;
        }

        public double getAllow()
        {
            return this.allow;
        }
    }
}
