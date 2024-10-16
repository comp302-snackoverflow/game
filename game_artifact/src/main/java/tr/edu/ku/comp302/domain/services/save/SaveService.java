package tr.edu.ku.comp302.domain.services.save;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tr.edu.ku.comp302.domain.entity.FireBall;
import tr.edu.ku.comp302.domain.entity.Hex;
import tr.edu.ku.comp302.domain.entity.Lance;
import tr.edu.ku.comp302.domain.entity.Remain;
import tr.edu.ku.comp302.domain.entity.SpellBox;
import tr.edu.ku.comp302.domain.entity.barrier.Barrier;
import tr.edu.ku.comp302.domain.handler.DatabaseHandler;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;

import java.util.List;

public class SaveService {
    private static final Logger logger = LogManager.getLogger(SaveService.class);
    private static SaveService instance;
    private final DatabaseHandler dbHandler;

    private SaveService() {
        dbHandler = DatabaseHandler.getInstance();
    }

    public static SaveService getInstance() {
        if (instance == null) {
            instance = new SaveService();
        }
        return instance;
    }

    public boolean saveGame(FireBall fireball, Lance lance, List<Barrier> barriers, List<Remain> remains, int score, List<Hex> hexes, List<SpellBox> spellBoxes) {
        FireballData fireballData = getFireballData(fireball, LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight());
        LanceData lanceData = getLanceData(lance, LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight());
        List<BarrierData> barrierData = barriers.stream().map(barrier -> getBarrierData(barrier, LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight())).toList();
        List<RemainData> remainData = remains.stream().map(remain -> getRemainData(remain, LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight())).toList();
        List<HexData> hexData = hexes.stream().map(hex -> getHexData(hex, LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight())).toList();
        List<SpellBoxData> spellBoxData = spellBoxes.stream().map(spellBox -> getSpellBoxData(spellBox, LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight())).toList();
        GameData data = new GameData(fireballData, lanceData, barrierData, remainData, hexData, spellBoxData, score);

        return dbHandler.saveGame(data);
    }

    public boolean saveMap(List<Barrier> barriers) {
        return saveMap(barriers, LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight());
    }

    public boolean saveMap(List<Barrier> barriers, double width, double height) {
        List<BarrierData> barrierData = barriers.stream().map(barrier -> getBarrierData(barrier, width, height)).toList();
        return dbHandler.saveMap(barrierData);
    }

    private FireballData getFireballData(FireBall fireball, double width, double height) {
        fireball.adjustPositionAndSpeed(width, height, 1, 1);
        double x = fireball.getXPosition();
        double y = fireball.getYPosition();
        double dx = fireball.getDx();
        double dy = fireball.getDy();
        // Retrieve the old size in case the user resumes the game
        fireball.adjustPositionAndSpeed(1, 1, width, height);
        return new FireballData(x, y, dx, dy);
    }

    private LanceData getLanceData(Lance lance, double width, double height) {
        lance.adjustPositionAndSize(width, height, 1, 1);
        double x = lance.getXPosition();
        double y = lance.getYPosition();
        double angle = lance.getRotationAngle();
        lance.adjustPositionAndSize(1, 1, width, height);
        return new LanceData(x, y, angle);
    }

    private BarrierData getBarrierData(Barrier barrier, double width, double height) {
        barrier.adjustPositionAndSize(width, height, 1, 1);
        double x = barrier.getXPosition();
        double y = barrier.getYPosition();
        int health = barrier.getHealth();
        String type = barrier.getClass().getSimpleName();
        int barrierID = dbHandler.getBarrierIdFromType(type);
        barrier.adjustPositionAndSize(1, 1, width, height);
        return new BarrierData(x, y, health, barrierID);
    }

    private RemainData getRemainData(Remain remain, double width, double height) {
        remain.updatePositionRelativeToScreen(LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight(), 1, 1);
        double x = remain.getXPosition();
        double y = remain.getYPosition();
        boolean isDropped = remain.isDropped();
        remain.updatePositionRelativeToScreen(1, 1, width, height);
        return new RemainData(x, y, isDropped);
    }

    private HexData getHexData(Hex hex, double width, double height) {
        hex.updatePositionRelativeToScreen(LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight(), 1, 1);
        double x = hex.getXPosition();
        double y = hex.getYPosition();
        double rotationAngle = hex.getRotationAngle();
        hex.updatePositionRelativeToScreen(1, 1, width, height);
        return new HexData(x, y, rotationAngle);
    }


    private SpellBoxData getSpellBoxData(SpellBox spellBox, double width, double height) {
        spellBox.updatePositionRelativeToScreen(LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight(), 1, 1);
        double x = spellBox.getXPosition();
        double y = spellBox.getYPosition();
        boolean isDropped = spellBox.isDropped();
        spellBox.updatePositionRelativeToScreen(1, 1, width, height);
        return new SpellBoxData(x, y, isDropped);
    }
}
