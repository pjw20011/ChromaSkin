package PibuStory.makeup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MakeupService {

    @Autowired
    private FoundationGreatRepository foundationGreatRepository;

    @Autowired
    private LipstickGreatRepository lipstickGreatRepository;

    public void saveFoundationSelection(String foundationLabel, String color, String nickname) {
        FoundationGreat foundationGreat = new FoundationGreat();
        foundationGreat.setFoundationLabel(foundationLabel);
        foundationGreat.setColor(color);
        foundationGreat.setNickname(nickname);
        foundationGreatRepository.save(foundationGreat); // 저장
    }


    public void saveLipstickSelection(String lipstickId, String colorImage, String lipName, String lipBrand, String lipColorCode, String nickname) {
        LipstickGreat lipstickGreat = new LipstickGreat();
        lipstickGreat.setLipstickId(lipstickId);
        lipstickGreat.setColorImage(colorImage);
        lipstickGreat.setLipName(lipName);
        lipstickGreat.setLipBrand(lipBrand);
        lipstickGreat.setLipColorCode(lipColorCode);
        lipstickGreat.setNickname(nickname);
        lipstickGreatRepository.save(lipstickGreat); // 저장
    }
}
