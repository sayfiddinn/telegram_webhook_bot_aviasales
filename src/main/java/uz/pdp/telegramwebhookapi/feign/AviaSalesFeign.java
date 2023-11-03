package uz.pdp.telegramwebhookapi.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import uz.pdp.telegramwebhookapi.payload.Aerodrome;
import uz.pdp.telegramwebhookapi.payload.Country;
import uz.pdp.telegramwebhookapi.payload.DTO.AerodromeDTO;
import uz.pdp.telegramwebhookapi.payload.DTO.PlaneDTO;
import uz.pdp.telegramwebhookapi.payload.InlineDTO;
import uz.pdp.telegramwebhookapi.payload.Plane;
import uz.pdp.telegramwebhookapi.util.Utils;

import java.util.List;
import java.util.UUID;

@Service
@FeignClient(url = Utils.AVIA_BASE_URL,name = "FeignAvia")
public interface AviaSalesFeign {
    @PostMapping("api/admin/planes")
    ResponseEntity<?> addPlane(@RequestBody PlaneDTO planeDTO, @RequestHeader("authorization") String headerValue);
    @GetMapping("api/admin/planes")
    ResponseEntity<List<InlineDTO>> getAllPlane(@RequestHeader("authorization") String headerValue);
    @GetMapping("api/admin/planes/{id}")
    ResponseEntity<Plane> getPlaneById(@RequestHeader("authorization") String headerValue, @PathVariable UUID id);
    @PostMapping("api/admin/aerodrome")
    ResponseEntity<?> addAerodrome(@RequestBody AerodromeDTO aerodromeDTO, @RequestHeader("authorization") String headerValue);
    @GetMapping("api/admin/country")
    ResponseEntity<List<Country>> getCountry(@RequestHeader("authorization") String headerValue);
    @GetMapping("api/admin/aerodrome")
    ResponseEntity<List<InlineDTO>> getAllAerodrome(@RequestHeader("authorization") String headerValue);
    @GetMapping("api/admin/aerodrome/{id}")
    ResponseEntity<Aerodrome> getAerodromeById(@RequestHeader("authorization") String headerValue, @PathVariable UUID id);
    @DeleteMapping("api/admin/aerodrome/{id}")
    ResponseEntity<Aerodrome> deleteAerodromeById(@RequestHeader("authorization") String headerValue, @PathVariable UUID id);
    @DeleteMapping("api/admin/planes/{id}")
    ResponseEntity<Plane> deletePlaneById(@RequestHeader("authorization") String headerValue, @PathVariable UUID id);
    @PutMapping("api/admin/aerodrome/{id}")
    ResponseEntity<Aerodrome> editAerodrome(@RequestHeader("authorization") String headerValue, @PathVariable UUID id,
                                                  @RequestBody AerodromeDTO aerodromeDTO);

    @PutMapping("api/admin/planes/{id}")
    ResponseEntity<?> editePlane(@RequestHeader("authorization") String headerValue, @PathVariable UUID id,
                                 @RequestBody PlaneDTO planeDTO);

}
