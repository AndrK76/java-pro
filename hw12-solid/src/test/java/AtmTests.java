import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.andrk.atm.AtmImpl;
import ru.otus.andrk.domain.*;
import ru.otus.andrk.rubles.Rubles;
import ru.otus.andrk.utils.Banknotes;
import ru.otus.andrk.utils.BanknotesHandler;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AtmTests {
    @Test
    @DisplayName("Проверка на возможность загрузки банкнот в банкомат")
    void canPutMoneyToAtmTest() {
        Atm atm = AtmImpl.create(
                new AtmCellDefinition(Rubles.getByNominal(5), 100),
                new AtmCellDefinition(Rubles.getByNominal(50), 100),
                new AtmCellDefinition(Rubles.getByNominal(200), 20),
                new AtmCellDefinition(Rubles.getByNominal(1000), 200)
        );
        var expectedSumInAtm = 0;

        atm.putMoneyToAtm(BanknotesHandler.toMap(
                new Banknotes(Rubles.getByNominal(50), 10),
                new Banknotes(Rubles.getByNominal(200), 10),
                new Banknotes(Rubles.getByNominal(1000), 10)
        ));
        expectedSumInAtm = 12_500;
        assertThat(atm.getMoneyLeftInAtm()).isEqualTo(expectedSumInAtm);

        atm.putMoneyToAtm(BanknotesHandler.toMap(new Banknotes(Rubles.getByNominal(50), 1)));
        atm.putMoneyToAtm(BanknotesHandler.toMap(new Banknotes(Rubles.getByNominal(5), 1)));
        expectedSumInAtm = 12_555;
        assertThat(atm.getMoneyLeftInAtm()).isEqualTo(expectedSumInAtm);
    }

    @Test
    @DisplayName("Загрузка в банкомат купюр, для которых нет ячейки")
    void putMoneyForAtmInNoExistCellTest() {
        Atm atm = AtmImpl.create(
                new AtmCellDefinition(Rubles.getByNominal(5), 100),
                new AtmCellDefinition(Rubles.getByNominal(10), 100)
        );
        var expectedSumInAtm = 0;

        assertThrows(NoCellForBanknoteException.class, () ->
                atm.putMoneyToAtm(BanknotesHandler.toMap(
                        new Banknotes(Rubles.getByNominal(5), 11),
                        new Banknotes(Rubles.getByNominal(10), 1),
                        new Banknotes(Rubles.getByNominal(100), 11)
                ))
        );
        assertThat(atm.getMoneyLeftInAtm()).isEqualTo(expectedSumInAtm);

        atm.putMoneyToAtm(BanknotesHandler.toMap(
                new Banknotes(Rubles.getByNominal(5), 11),
                new Banknotes(Rubles.getByNominal(10), 1)
        ));
        expectedSumInAtm = 65;
        assertThat(atm.getMoneyLeftInAtm()).isEqualTo(expectedSumInAtm);

        assertThrows(NoCellForBanknoteException.class, () ->
                atm.putMoneyToAtm(BanknotesHandler.toMap(
                        new Banknotes(Rubles.getByNominal(5), 11),
                        new Banknotes(Rubles.getByNominal(10), 1),
                        new Banknotes(Rubles.getByNominal(100), 11)
                ))
        );

        atm.putMoneyToAtm(BanknotesHandler.toMap(new Banknotes(Rubles.getByNominal(10), 1)));
        expectedSumInAtm = 75;
        assertThat(atm.getMoneyLeftInAtm()).isEqualTo(expectedSumInAtm);
    }

    @Test
    @DisplayName("Загрузка в банкомат суммы превышающая емкость ячейки")
    void putMoneyOverFullTest() {
        Atm atm = AtmImpl.create(
                new AtmCellDefinition(Rubles.getByNominal(1000), 20),
                new AtmCellDefinition(Rubles.getByNominal(5000), 10)
        );
        var expectedSumInAtm = 0;

        assertThrows(AtmFullException.class, () ->
                atm.putMoneyToAtm(BanknotesHandler.toMap(new Banknotes(Rubles.getByNominal(1000), 22)))
        );
        assertThat(atm.getMoneyLeftInAtm()).isEqualTo(expectedSumInAtm);

        atm.putMoneyToAtm(BanknotesHandler.toMap(
                new Banknotes(Rubles.getByNominal(1000), 19),
                new Banknotes(Rubles.getByNominal(5000), 9)
        ));
        expectedSumInAtm = 64_000;
        assertThat(atm.getMoneyLeftInAtm()).isEqualTo(expectedSumInAtm);

        assertThrows(AtmFullException.class, () ->
                atm.putMoneyToAtm(BanknotesHandler.toMap(
                        new Banknotes(Rubles.getByNominal(1000), 2),
                        new Banknotes(Rubles.getByNominal(5000), 1)
                ))
        );
        assertThat(atm.getMoneyLeftInAtm()).isEqualTo(expectedSumInAtm);

        atm.putMoneyToAtm(BanknotesHandler.toMap(
                new Banknotes(Rubles.getByNominal(1000), 1),
                new Banknotes(Rubles.getByNominal(5000), 1)
        ));
        expectedSumInAtm = 70_000;
        assertThat(atm.getMoneyLeftInAtm()).isEqualTo(expectedSumInAtm);

        assertThrows(AtmFullException.class, () ->
                atm.putMoneyToAtm(BanknotesHandler.toMap(
                        new Banknotes(Rubles.getByNominal(1000), 1),
                        new Banknotes(Rubles.getByNominal(5000), 1)
                ))
        );
        assertThat(atm.getMoneyLeftInAtm()).isEqualTo(expectedSumInAtm);
    }


    private Atm getPreparedAtm() {
        Atm atm = AtmImpl.create(
                new AtmCellDefinition(Rubles.getByNominal(10), 100),
                new AtmCellDefinition(Rubles.getByNominal(100), 100),
                new AtmCellDefinition(Rubles.getByNominal(500), 100),
                new AtmCellDefinition(Rubles.getByNominal(1000), 100)
        );
        atm.putMoneyToAtm(BanknotesHandler.toMap(
                new Banknotes(Rubles.getByNominal(100), 20),
                new Banknotes(Rubles.getByNominal(500), 20),
                new Banknotes(Rubles.getByNominal(1000), 20)
        ));
        return atm;
    }


    @Test
    @DisplayName("Проверка на возможность получения денег из банкомата")
    void canGetMoneyFromAtm() {
        Atm atm = getPreparedAtm();
        var expectedSumInAtm = atm.getMoneyLeftInAtm();

        var expectedReceivedSum = 4_800;
        var received = atm.getMoneyFromAtm(expectedReceivedSum);
        assertThat(BanknotesHandler.sum(received)).isEqualTo(expectedReceivedSum);
        expectedSumInAtm -= expectedReceivedSum;
        assertThat(atm.getMoneyLeftInAtm()).isEqualTo(expectedSumInAtm);

        expectedReceivedSum = 1_300;
        received = atm.getMoneyFromAtm(expectedReceivedSum);
        assertThat(BanknotesHandler.sum(received)).isEqualTo(expectedReceivedSum);
        expectedSumInAtm -= expectedReceivedSum;
        assertThat(atm.getMoneyLeftInAtm()).isEqualTo(expectedSumInAtm);
    }

    @Test
    @DisplayName("Запрос отрицательной суммы")
    void getNegativeSumFromAtm() {
        Atm atm = getPreparedAtm();
        var expectedSumInAtm = atm.getMoneyLeftInAtm();

        assertThrows(IllegalArgumentException.class, () -> atm.getMoneyFromAtm(-2));
        assertThat(atm.getMoneyLeftInAtm()).isEqualTo(expectedSumInAtm);
    }

    @Test
    @DisplayName("Запрос суммы для которой требуется купюра в отсутствующей ячейке")
    void getMoneyFromAtm_NeedNominalWithNoExistCell() {
        Atm atm = getPreparedAtm();
        var expectedSumInAtm = atm.getMoneyLeftInAtm();
        assertThrows(NoHaveBanknoteException.class, () -> atm.getMoneyFromAtm(5));
        assertThat(atm.getMoneyLeftInAtm()).isEqualTo(expectedSumInAtm);
    }

    @Test
    @DisplayName("Запрос суммы для которой требуется купюра из пустой ячейки")
    void getMoneyFromAtm_NeedNominalWithEmptyCell() {
        Atm atm = getPreparedAtm();
        var expectedSumInAtm = atm.getMoneyLeftInAtm();
        assertThrows(NoHaveBanknoteException.class, () -> atm.getMoneyFromAtm(10));
        assertThat(atm.getMoneyLeftInAtm()).isEqualTo(expectedSumInAtm);
    }

    @Test
    @DisplayName("Запрос суммы большей чем остаток в банкомате")
    void getMoneyFromAtm_MoreThanExist() {
        Atm atm = getPreparedAtm();
        var expectedSumInAtm = atm.getMoneyLeftInAtm();

        {
            var receivedSum = expectedSumInAtm + 1000;
            assertThrows(NoHaveBanknoteException.class, () -> atm.getMoneyFromAtm(receivedSum));
            assertThat(atm.getMoneyLeftInAtm()).isEqualTo(expectedSumInAtm);
        }

        var expectedReceivedSum = 4_800;
        assertThat(BanknotesHandler.sum(atm.getMoneyFromAtm(expectedReceivedSum))).isEqualTo(expectedReceivedSum);
        expectedSumInAtm -= expectedReceivedSum;
        assertThat(atm.getMoneyLeftInAtm()).isEqualTo(expectedSumInAtm);

        {
            var receivedSum = expectedSumInAtm + 1000;
            assertThrows(NoHaveBanknoteException.class, () -> atm.getMoneyFromAtm(receivedSum));
            assertThat(atm.getMoneyLeftInAtm()).isEqualTo(expectedSumInAtm);
        }

    }
}
