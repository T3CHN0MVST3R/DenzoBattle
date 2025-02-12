# DenzoBattle

Проект, написанный в рамках итогового задания по курсу, где реализуется пошаговая стратегия в стиле «Герои меча и магии 3».

## Структура проекта

- **heroes** — папка с базовой игрой (GUI, графические ассеты, движок рендера и т.д.).
- **Проект студентам** — основной проект, где находится код реализации алгоритмической части:
  - `GeneratePresetImpl.java`
  - `SimulateBattleImpl.java`
  - `SuitableForAttackUnitsFinderImpl.java`
  - `UnitTargetPathFinderImpl.java`

## Функциональность

1. **GeneratePreset** (класс `GeneratePresetImpl`)
   - Генерирует армию противника, соблюдая лимит в 1500 очков и не более 11 юнитов на тип.
   - Использует жадный алгоритм на основе метрики эффективности (атака/стоимость, здоровье/стоимость).

2. **SuitableForAttackUnitsFinder** (класс `SuitableForAttackUnitsFinderImpl`)
   - Возвращает список юнитов, которых можно атаковать, учитывая, что часть юнитов может быть «закрыта» спереди/сзади.

3. **UnitTargetPathFinder** (класс `UnitTargetPathFinderImpl`)
   - Находит кратчайший путь на игровом поле (27×21), используя алгоритм BFS или Dijkstra/A* с учётом занятых клеток.

4. **SimulateBattle** (класс `SimulateBattleImpl`)
   - Осуществляет пошаговую симуляцию боя между армией игрока и компьютера.
   - Юниты сортируются по убыванию атаки, атакуют по очереди, погибшие исключаются.
