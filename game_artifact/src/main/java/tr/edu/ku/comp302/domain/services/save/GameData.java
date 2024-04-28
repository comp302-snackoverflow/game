package tr.edu.ku.comp302.domain.services.save;

import java.util.List;

public record GameData(
        FireballData fireballData,
        LanceData lanceData,
        List<BarrierData> barriersData,
        double score
) {
}
