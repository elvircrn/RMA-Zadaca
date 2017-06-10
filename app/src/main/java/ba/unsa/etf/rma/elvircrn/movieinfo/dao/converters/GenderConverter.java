package ba.unsa.etf.rma.elvircrn.movieinfo.dao.converters;

import android.arch.persistence.room.TypeConverter;

import ba.unsa.etf.rma.elvircrn.movieinfo.helpers.JHelpers;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Actor;

public class GenderConverter {
    @TypeConverter
    public static int fromGender(Actor.Gender gender) {
        return gender == null ? 0 : gender.ordinal();
    }

    @TypeConverter
    public Actor.Gender fromGenderIdToGender(int genderId) {
        return Actor.Gender.values()[genderId];
    }
}
