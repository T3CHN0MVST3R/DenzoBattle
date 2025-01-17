package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GeneratePresetImpl implements GeneratePreset {

    private static final int MAX_UNITS_PER_TYPE = 11;

    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        // 1. Подготовим список "прототипов" — в unitList обычно по 1 экземпляру каждого типа
        // (Допустим, там 4 юнита: Арчер, Пикинер, Мечник, Всадник).

        // 2. Считаем эффективность каждого из них
        List<Unit> prototypes = new ArrayList<>(unitList);
        prototypes.sort((u1, u2) -> {
            double eff1 = efficiency(u1);
            double eff2 = efficiency(u2);
            // сортируем по убыванию эффективности
            return Double.compare(eff2, eff1);
        });

        // 3. Набираем армию, начиная с наиболее эффективного типа
        List<Unit> resultUnits = new ArrayList<>();
        int currentPoints = 0;

        for (Unit proto : prototypes) {
            int count = 0;
            // пока можно добавить этого юнита, добавляем
            while (count < MAX_UNITS_PER_TYPE) {
                if (currentPoints + proto.getCost() > maxPoints) {
                    // если добавим - превысим лимит; выходим
                    break;
                }
                // Создаём "копию" юнита на базе прототипа
                Unit newUnit = copyUnit(proto);
                // Добавляем в итоговый список
                resultUnits.add(newUnit);
                currentPoints += newUnit.getCost();
                count++;
            }
        }

        // 4. Создаём объект Army с набранными юнитами
        Army computerArmy = new Army();
        computerArmy.setUnits(resultUnits);
        computerArmy.setPoints(currentPoints);

        // Возвращаем армию
        return computerArmy;
    }

    // Метрика "эффективности"
    private double efficiency(Unit u) {
        double attackEff = (double) u.getBaseAttack() / u.getCost();
        double healthEff = (double) u.getHealth() / u.getCost();
        // Возьмём среднее арифметическое
        return (attackEff + healthEff) / 2;
    }

    // Создаём копию юнита (условно "клонируем" базовые параметры)
    private Unit copyUnit(Unit proto) {
        Unit copy = new Unit();
        copy.setName("AI_" + proto.getUnitType() + "_" + System.nanoTime());
        copy.setUnitType(proto.getUnitType());
        copy.setBaseAttack(proto.getBaseAttack());
        copy.setHealth(proto.getHealth());
        copy.setCost(proto.getCost());
        copy.setAttackType(proto.getAttackType());
        // program и другие поля (по ситуации) можно скопировать или задать отдельно
        // Обычно для ИИ прописаны специальные программы:
        copy.setProgram(proto.getProgram());

        // isAlive по умолчанию true
        copy.setAlive(true);

        return copy;
    }
}
