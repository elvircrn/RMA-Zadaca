package ba.unsa.etf.rma.elvircrn.movieinfo.mappers;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ba.unsa.etf.rma.elvircrn.movieinfo.models.Director;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.CrewItemDTO;

public class DirectorMapper {
    public static final String DIRECTOR_ROLE = "Director";

    public static Director toDirector(CrewItemDTO crewItemDTO) {
        Director director = new Director();
        director.setId(crewItemDTO.getId());
        director.setName(crewItemDTO.getName());
        return director;
    }

    public static ArrayList<Director> toDirectors(List<CrewItemDTO> crewItemDTOList) {
        if (crewItemDTOList == null || crewItemDTOList.size() == 0)
            return new ArrayList<Director>();

        ArrayList<Director> ret = new ArrayList<>();

        for (CrewItemDTO crewItemDTO : crewItemDTOList)
            if (Objects.equals(crewItemDTO.getJob(), DIRECTOR_ROLE))
                ret.add(toDirector(crewItemDTO));

        return ret;
    }

}
