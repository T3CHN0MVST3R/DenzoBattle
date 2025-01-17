package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.List;

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {

    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        // Список подходящих юнитов, которые не «закрыты»
        List<Unit> suitable = new ArrayList<>();

        // Идём по каждому ряду (3 ряда по условию)
        for (List<Unit> row : unitsByRow) {
            // Пропускаем пустые ряды
            if (row.isEmpty()) {
                continue;
            }

            // Берём для сравнения первый юнит в ряду
            Unit chosen = row.get(0);

            for (Unit current : row) {
                if (isLeftArmyTarget) {
                    // isLeftArmyTarget = true -> цель: левая армия ->
                    // нужен юнит, не закрытый слева -> выбираем с минимальной координатой Y
                    if (current.getY() < chosen.getY()) {
                        chosen = current;
                    }
                } else {
                    // isLeftArmyTarget = false -> цель: правая армия ->
                    // нужен юнит, не закрытый справа -> выбираем с максимальной координатой Y
                    if (current.getY() > chosen.getY()) {
                        chosen = current;
                    }
                }
            }

            // Добавляем найденного «крайнего» юнита в итоговый список
            suitable.add(chosen);
        }

        return suitable;
    }
}
