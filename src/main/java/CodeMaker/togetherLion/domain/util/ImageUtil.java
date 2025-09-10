package CodeMaker.togetherLion.domain.util;

import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Component
public class ImageUtil {

    // 원본 Base64 이미지 스트링에 Base64 워터마크 이미지 스트링 합성
    public String addWatermarkToBase64Image(String originalBase64Image, String waterMarkBase64) throws IOException {

        // ## Base64 접두어 (Data URL 스키마)가 붙어있다면 제거 ##
        if (originalBase64Image != null && originalBase64Image.startsWith("data:")) {
            originalBase64Image = originalBase64Image.substring(originalBase64Image.indexOf(",") + 1);
        }
        if (waterMarkBase64 != null && waterMarkBase64.startsWith("data:")) {
            waterMarkBase64 = waterMarkBase64.substring(waterMarkBase64.indexOf(",") + 1);
        }

        // 1. Base64 스트링을 바이트 배열로 디코딩 (각각)
        byte[] originalBytes = Base64.getDecoder().decode(originalBase64Image);
        byte[] watermarkBytes = Base64.getDecoder().decode(waterMarkBase64);

        // 2. 바이트 배열을 BufferedImage 객체로 변환
        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(originalBytes));
        BufferedImage watermarkImage = ImageIO.read(new ByteArrayInputStream(watermarkBytes));

        if (originalImage == null) {
            throw new IOException("원본 이미지를 읽을 수 없습니다. 유효한 이미지 Base64인지 확인해주세요.");
        }
        if (watermarkImage == null) {
            throw new IOException("워터마크 이미지를 읽을 수 없습니다. 유효한 이미지 Base64인지 확인해주세요.");
        }

        // 3. 워터마크를 합성할 BufferedImage 생성 (원본 이미지와 같은 타입으로)
        // 예를 들어 ARGB (알파 채널 포함) 타입을 쓰는 게 투명도 처리에 유리함
        BufferedImage combinedImage = new BufferedImage(
                originalImage.getWidth(),
                originalImage.getHeight(),
                BufferedImage.TYPE_INT_ARGB // 알파 채널 포함, 투명도 지원
        );

        // 4. Graphics2D 객체를 얻어서 그림 그리기
        Graphics2D g = (Graphics2D) combinedImage.getGraphics();

        // 품질 설정 (옵션)
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // 원본 이미지를 먼저 그림
        g.drawImage(originalImage, 0, 0, null);

        // 워터마크의 투명도 설정 (예: 50% 투명도)
        // 만약 워터마크 이미지 자체에 투명도가 있다면 이 설정은 필요 없을 수 있음.
        // 여기선 추가적인 투명도를 주는 예시임.
        float alpha = 0.5f; // 0.0f (완전 투명) ~ 1.0f (완전 불투명)
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        // 워터마크 크기 및 위치 조정 (예: 우측 하단에 작게)
        int watermarkWidth = originalImage.getWidth() / 3; // 원본 이미지 가로의 1/3 크기
        int watermarkHeight = originalImage.getHeight() / 3; // 원본 이미지 세로의 1/3 크기

        // 워터마크 이미지 비율 유지를 위한 계산
        double watermarkRatio = (double) watermarkImage.getWidth() / watermarkImage.getHeight();
        if ((double) watermarkWidth / watermarkHeight > watermarkRatio) {
            watermarkWidth = (int) (watermarkHeight * watermarkRatio);
        } else {
            watermarkHeight = (int) (watermarkWidth / watermarkRatio);
        }

        // 워터마크 위치 설정 (예: 우측 하단, 10px 여백)
        int x = (originalImage.getWidth() - watermarkWidth) / 2;    // 가로 중앙 계산
        int y = (originalImage.getHeight() - watermarkHeight) / 2; // 세로 중앙 계산

        // 워터마크 이미지를 그림 (지정한 크기와 위치로)
        g.drawImage(watermarkImage, x, y, watermarkWidth, watermarkHeight, null);

        g.dispose(); // 리소스 해제

        // 5. 합성된 BufferedImage를 다시 바이트 배열로 변환
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(combinedImage, "png", baos); // 이미지 형식은 원본에 따라 "jpg", "gif" 등으로 바꿔도 됨. PNG가 투명도 지원.
        byte[] resultBytes = baos.toByteArray();

        // 6. 바이트 배열을 Base64 스트링으로 인코딩
        String encodedString = Base64.getEncoder().encodeToString(resultBytes);

        // 6. 바이트 배열을 Base64 스트링으로 인코딩
        return "data:image/png;base64," + encodedString;
    }
}
