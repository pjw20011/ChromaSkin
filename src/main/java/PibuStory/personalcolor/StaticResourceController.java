package PibuStory.personalcolor;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.MalformedURLException;

@Controller
public class StaticResourceController {

    @GetMapping(value = "/output_image/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveImage(@PathVariable String filename) {
        try {
            // 절대 경로로 파일을 지정하고 URI로 변환
            Path file = Paths.get("Demo_Ver/Demo_ver/src/main/resources/static/output_image/").resolve(filename).toAbsolutePath();
            Resource resource = new UrlResource(file.toUri());

            // 파일 존재 여부와 읽기 가능 여부 확인
            if (!resource.exists() || !resource.isReadable()) {
                throw new RuntimeException("파일을 찾을 수 없거나 읽을 수 없습니다: " + file.toString());
            }

            // 캐시 제어 설정 후 리소스 반환
            return ResponseEntity.ok()
                    .cacheControl(CacheControl.noCache().cachePrivate())
                    .body(resource);
        } catch (MalformedURLException e) {
            throw new RuntimeException("잘못된 파일 경로입니다: " + filename, e);
        }
    }
}
