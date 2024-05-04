package tn.esprit.rh.achat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tn.esprit.rh.achat.entities.CategorieProduit;
import tn.esprit.rh.achat.entities.Produit;
import tn.esprit.rh.achat.repositories.CategorieProduitRepository;
import tn.esprit.rh.achat.repositories.ProduitRepository;
import tn.esprit.rh.achat.repositories.StockRepository;
import tn.esprit.rh.achat.services.ProduitServiceImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
@ContextConfiguration(classes = {ProduitServiceImpl.class})
@ExtendWith(SpringExtension.class)
public class ProduitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
   private CategorieProduitRepository categorieProduitRepository;

    @MockBean
    private ProduitRepository produitRepository;

    @Autowired
    private ProduitServiceImpl produitServiceImpl;

    @MockBean
    private StockRepository stockRepository;
    @Test
    void testRetrieveAllProduits() {
        ArrayList<Produit> produitList = new ArrayList<>();
        when(produitRepository.findAll()).thenReturn(produitList);
        List<Produit> actualRetrieveAllProduitsResult = produitServiceImpl.retrieveAllProduits();
        assertSame(produitList, actualRetrieveAllProduitsResult);
        assertTrue(actualRetrieveAllProduitsResult.isEmpty());
        verify(produitRepository).findAll();
    }

    @Test
    void testDeleteProduit() {
        doNothing().when(produitRepository).deleteById((Long) any());
        produitServiceImpl.deleteProduit(123L);
        verify(produitRepository).deleteById((Long) any());
    }

    @Test
    public void testRetrieveCategorieProduit() throws Exception {
        // Arrange
        Long id = 1L; // Assuming there is a category with this ID in the database
        CategorieProduit categorieProduit = new CategorieProduit();
        categorieProduit.setIdCategorieProduit(id);
        when(categorieProduitRepository.findById(id)).thenReturn(java.util.Optional.of(categorieProduit));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/categorieProduit/retrieve-categorieProduit/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.idCategorieProduit").value(id));

        // Verify
        verify(categorieProduitRepository).findById(id);
    }

    @Test
    public void testAddCategorieProduit() throws Exception {
        // Arrange
        CategorieProduit categorieProduit = new CategorieProduit();
        categorieProduit.setCodeCategorie("TestCode");
        categorieProduit.setLibelleCategorie("TestLibelle");

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/categorieProduit/add-categorieProduit")
                        .content(asJsonString(categorieProduit))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.codeCategorie").value("TestCode"));

        // Verify
        verify(categorieProduitRepository).save(categorieProduit);
    }

    // Utility method to convert object to JSON string
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
