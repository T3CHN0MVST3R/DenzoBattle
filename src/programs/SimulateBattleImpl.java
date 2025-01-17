package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SimulateBattleImpl implements SimulateBattle {

    // Обычно нам дают PrintBattleLog в конструкторе или через сеттер
    private final PrintBattleLog printBattleLog;

    public SimulateBattleImpl(PrintBattleLog printBattleLog) {
        this.printBattleLog = printBattleLog;
    }

    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
        // Пока и у игрока, и у компьютера есть живые
        while (hasAliveUnits(playerArmy) && hasAliveUnits(computerArmy)) {
            // Удаляем мертвых и сортируем
            List<Unit> playerUnits = getSortedAliveUnits(playerArmy);
            List<Unit> computerUnits = getSortedAliveUnits(computerArmy);

            // Если кто-то остался без юнитов — завершаем бой
            if (playerUnits.isEmpty() || computerUnits.isEmpty()) {
                break;
            }

            // Формируем общую очередь ходов:
            // Вариант 1: чередуем (player, computer, player, computer...)
            // При этом у нас может быть разное количество юнитов, поэтому внимательнее
            List<Unit> turnOrder = mergeUnitsByStrongestFirst(playerUnits, computerUnits);

            // Проходим по очереди
            for (Unit unit : turnOrder) {
                // Проверяем, жив ли юнит
                if (!unit.isAlive()) {
                    continue;
                }

                // Юнит ходит: атакует
                Unit target = unit.getProgram().attack();  // может вернуть null
                if (target != null) {
                    // Лог
                    printBattleLog.printBattleLog(unit, target);
                }
            }
            // Один раунд закончен

            // делать задержку для наглядности
            Thread.sleep(300); // simSpeed можно учитывать, если нужно

            // После раунда кто-то мог умереть, следующая итерация while всё проверит
        }
    }

    private boolean hasAliveUnits(Army army) {
        for (Unit u : army.getUnits()) {
            if (u.isAlive()) {
                return true;
            }
        }
        return false;
    }

    private List<Unit> getSortedAliveUnits(Army army) {
        List<Unit> alive = new ArrayList<>();
        for (Unit u : army.getUnits()) {
            if (u.isAlive()) {
                alive.add(u);
            }
        }
        // Сортируем по убыванию атаки
        alive.sort(Comparator.comparingInt(Unit::getBaseAttack).reversed());
        return alive;
    }

    // Пример чередования: смотрим списки, "самый большой baseAttack" ходит первым
    private List<Unit> mergeUnitsByStrongestFirst(List<Unit> listA, List<Unit> listB) {
        // Можно просто объединить, а потом отсортировать по baseAttack
        List<Unit> merged = new ArrayList<>(listA);
        merged.addAll(listB);
        merged.sort(Comparator.comparingInt(Unit::getBaseAttack).reversed());
        return merged;
    }
}
