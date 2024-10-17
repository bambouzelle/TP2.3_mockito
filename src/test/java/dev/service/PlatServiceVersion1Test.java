package dev.service;

import dev.dao.IPlatDao;
import dev.entite.Plat;
import dev.exception.PlatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlatServiceVersion1Test {

    @Mock
    private IPlatDao platDao; // Simulation de l'interface IPlatDao

    @InjectMocks
    private PlatServiceVersion1 platService; // Injection du mock dans PlatServiceVersion1

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialisation des mocks
    }

    // Test pour vérifier que les noms des plats sont en majuscules
    @Test
    void listerPlats_shouldReturnUpperCasePlats() {
        // Simuler le retour d'une liste de plats du DAO
        List<Plat> plats = Arrays.asList(new Plat("poulet", 1200), new Plat("riz", 900));
        when(platDao.listerPlats()).thenReturn(plats); // Simuler le comportement du DAO

        // Appeler la méthode à tester
        List<Plat> result = platService.listerPlats();

        // Vérifier que les noms des plats sont bien transformés en majuscules
        List<String> expectedPlats = Arrays.asList("POULET", "RIZ");
        List<String> actualPlats = result.stream().map(Plat::getNom).toList(); // Extraction des noms

        assertEquals(expectedPlats, actualPlats); // Vérification que les noms sont en majuscules
        verify(platDao, times(1)).listerPlats(); // Vérifier que la méthode listerPlats() du DAO a été appelée une fois
    }

    // Test pour vérifier qu'une exception PlatException est levée lorsque le DAO retourne une NullPointerException
    @Test
    void listerPlats_shouldThrowPlatExceptionWhenDaoThrowsNullPointerException() {
        // Simuler le comportement du DAO pour lever une NullPointerException
        when(platDao.listerPlats()).thenThrow(new NullPointerException());

        // Vérifier que la méthode listerPlats() du service lève une PlatException
        PlatException exception = assertThrows(PlatException.class, () -> {
            platService.listerPlats();
        });

        // Vérifier que la cause de l'exception est une NullPointerException
        assertTrue(exception.getCause() instanceof NullPointerException);
        assertEquals("Erreur : liste de plats vide", exception.getMessage()); // Message attendu
    }
}

