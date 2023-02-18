import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.andrk.rubles.Rubles;
import ru.otus.andrk.utils.Banknotes;
import ru.otus.andrk.utils.BanknotesHandler;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BanknotesHandlerTests {
    List<Banknotes> sample;

    @BeforeEach
    void makeSample() {
        var arrBanknotes = new Banknotes[]{
                new Banknotes(Rubles.getByNominal(10), 5),
                new Banknotes(Rubles.getByNominal(100), 2),
                new Banknotes(Rubles.getByNominal(1000), 3),
        };
        sample = Arrays.asList(arrBanknotes);
    }

    @Test
    @DisplayName("Проверка BanknotesHandler.toListBanknote()")
    void toListBanknoteTest() {
        var banknoteList = BanknotesHandler.toListBanknote(sample);
        assertThat(banknoteList.size()).isEqualTo(10);
        assertThat((int) (banknoteList.stream().filter(r -> r.nominal() == 10).count())).isEqualTo(5);
        assertThat((int) (banknoteList.stream().filter(r -> r.nominal() == 100).count())).isEqualTo(2);
        assertThat((int) (banknoteList.stream().filter(r -> r.nominal() == 1000).count())).isEqualTo(3);
    }


    @Test
    @DisplayName("Проверка BanknotesHandler.groupByBanknote()")
    void groupByBanknoteTest() {
        var sample = BanknotesHandler.toListBanknote(this.sample);
        var banknotesList = BanknotesHandler.groupByBanknote(sample);
        assertThat(banknotesList.size()).isEqualTo(3);
        assertThat(banknotesList.stream().filter(r -> r.banknote().nominal() == 10).toArray())
                .anyMatch(r -> ((Banknotes) r).count() == 5)
                .hasSize(1);
        assertThat(banknotesList.stream().filter(r -> r.banknote().nominal() == 100).toArray())
                .anyMatch(r -> ((Banknotes) r).count() == 2)
                .hasSize(1);
        assertThat(banknotesList.stream().filter(r -> r.banknote().nominal() == 1000).toArray())
                .anyMatch(r -> ((Banknotes) r).count() == 3)
                .hasSize(1);
    }

    @Test
    @DisplayName("Проверка BanknotesHandler.sum()")
    void sumTest() {
        var sample = BanknotesHandler.toListBanknote(this.sample);
        assertThat(BanknotesHandler.sum(sample)).isEqualTo(3_250);
        sample.add(Rubles.getByNominal(5000));
        assertThat(BanknotesHandler.sum(sample)).isEqualTo(8_250);
    }
}
