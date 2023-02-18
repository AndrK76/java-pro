import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.andrk.atm.AtmCellImpl;
import ru.otus.andrk.domain.AtmCell;
import ru.otus.andrk.domain.CellFullException;
import ru.otus.andrk.domain.NoHaveBanknoteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AtmCellTest {

    @Test
    @DisplayName("Проверка пополнения ячейки")
    void cellPutTest() {
        AtmCell cell = new AtmCellImpl(5);
        assertThat(cell.count()).isEqualTo(0);
        cell.add(1);
        assertThat(cell.count()).isEqualTo(1);
        cell.tryAdd(2);
        assertThat(cell.count()).isEqualTo(1);
        cell.add(2);
        assertThat(cell.count()).isEqualTo(3);
        assertThrows(CellFullException.class, () -> cell.tryAdd(3));
        assertThat(cell.count()).isEqualTo(3);
        assertThrows(CellFullException.class, () -> cell.add(3));
        assertThat(cell.count()).isEqualTo(3);
        assertThrows(IllegalArgumentException.class, () -> cell.tryAdd(-1));
        assertThat(cell.count()).isEqualTo(3);
        assertThrows(IllegalArgumentException.class, () -> cell.add(-1));
        assertThat(cell.count()).isEqualTo(3);
    }

    @Test
    @DisplayName("Проверка пополнения ячейки")
    void cellGetTest() {
        AtmCell cell = new AtmCellImpl(5);
        cell.add(5);
        assertThat(cell.count()).isEqualTo(5);
        cell.get(1);
        assertThat(cell.count()).isEqualTo(4);
        cell.tryGet(1);
        assertThat(cell.count()).isEqualTo(4);
        cell.get(2);
        assertThat(cell.count()).isEqualTo(2);
        assertThrows(NoHaveBanknoteException.class, () -> cell.tryGet(3));
        assertThat(cell.count()).isEqualTo(2);
        assertThrows(NoHaveBanknoteException.class, () -> cell.get(3));
        assertThat(cell.count()).isEqualTo(2);
        assertThrows(IllegalArgumentException.class, () -> cell.tryGet(-1));
        assertThat(cell.count()).isEqualTo(2);
        assertThrows(IllegalArgumentException.class, () -> cell.get(-1));
        assertThat(cell.count()).isEqualTo(2);
    }
}
