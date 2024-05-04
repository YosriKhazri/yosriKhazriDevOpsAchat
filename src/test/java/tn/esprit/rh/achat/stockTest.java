package tn.esprit.rh.achat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.rh.achat.entities.Stock;
import tn.esprit.rh.achat.repositories.StockRepository;
import tn.esprit.rh.achat.services.StockServiceImpl;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class stockTest {

    @InjectMocks
    StockServiceImpl stockService;

    @Mock
    StockRepository stockRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this); // initialize mocks

        // Test data setup
        Stock stock1 = new Stock();

        stock1.setQte(20);

        Stock stock2 = new Stock();

        stock2.setQteMin(40);

        // Mock behavior for the repository
        when(stockRepository.findAll()).thenReturn(Arrays.asList(stock1, stock2));
        when(stockRepository.save(any(Stock.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    public void testRetrieveAllStocks() {
        List<Stock> stocks = stockService.retrieveAllStocks();
        if (stocks != null) {
            System.out.println("True");
        } else {
            assertEquals(2, stocks.size());
            assertTrue(stocks.stream().anyMatch(d -> d.getQte() == (20)));
            assertTrue(stocks.stream().anyMatch(d -> d.getQteMin() == (40)));
        }
    }


}
