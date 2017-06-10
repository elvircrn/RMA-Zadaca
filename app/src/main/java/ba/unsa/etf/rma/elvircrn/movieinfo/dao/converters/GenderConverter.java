package ba.unsa.etf.rma.elvircrn.movieinfo.dao.converters;

import android.arch.persistence.room.TypeConverter;

import ba.unsa.etf.rma.elvircrn.movieinfo.models.Actor;

public class GenderConverter {
    @TypeConverter
    public static int fromGender(Actor.Gender gender) {
        switch (gender) {
            case FEMALE:
                return 0;
            case MALE:
                return 1;
            case NONBINARY:
                return 2;
        }
        return -1;
    }

    @TypeConverter
    public Actor.Gender fromGenderIdToGender(int genderId) {
        return Actor.Gender.values()[genderId];
    }
}
