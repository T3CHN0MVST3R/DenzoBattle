package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.UnitTargetPathFinder;
import com.battle.heroes.army.map.Edge;

import java.util.*;

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {

    // Размер игрового поля
    private static final int WIDTH = 27;
    private static final int HEIGHT = 21;

    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {
        // 1. Проверяем, не совпадают ли координаты атакующего и цели
        int startX = attackUnit.getX();
        int startY = attackUnit.getY();
        int goalX = targetUnit.getX();
        int goalY = targetUnit.getY();
        if (startX == goalX && startY == goalY) {
            // Уже на месте
            return Collections.singletonList(new Edge(startX, startY));
        }

        // 2. Собираем в набор "препятствий" координаты занятых клеток
        Set<String> occupied = new HashSet<>();
        for (Unit u : existingUnitList) {
            if (u.isAlive()) {
                occupied.add(u.getX() + "_" + u.getY());
            }
        }

        // 3. Инициализируем структуру для BFS
        //    visited[x][y] = true, если мы уже посетили клетку (x, y)
        boolean[][] visited = new boolean[WIDTH][HEIGHT];

        // Предок (x, y) -> (parentX, parentY), чтобы восстанавливать путь
        Map<String, String> parent = new HashMap<>();

        // Очередь клеток для обхода в ширину
        Queue<int[]> queue = new LinkedList<>();

        // Помечаем старт
        visited[startX][startY] = true;
        queue.add(new int[]{startX, startY});

        // 4. BFS
        int[][] directions = {
                {1, 0},  // вправо
                {-1, 0}, // влево
                {0, 1},  // вверх
                {0, -1}  // вниз
        };

        boolean found = false;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int cx = current[0];
            int cy = current[1];

            if (cx == goalX && cy == goalY) {
                // Нашли путь до цели
                found = true;
                break;
            }

            // Смотрим все 4 соседние клетки
            for (int[] d : directions) {
                int nx = cx + d[0];
                int ny = cy + d[1];

                // Проверяем границы
                if (nx < 0 || nx >= WIDTH || ny < 0 || ny >= HEIGHT) {
                    continue;
                }
                // Проверяем, не занято ли
                if (occupied.contains(nx + "_" + ny)) {
                    continue;
                }
                // Проверяем, не посещали ли ранее
                if (!visited[nx][ny]) {
                    visited[nx][ny] = true;
                    parent.put(nx + "_" + ny, cx + "_" + cy);
                    queue.add(new int[]{nx, ny});
                }
            }
        }

        // 5. Если путь не найден — возвращаем пустой список
        if (!found) {
            return Collections.emptyList();
        }

        // 6. Восстанавливаем путь "с конца к началу"
        List<Edge> path = new ArrayList<>();
        // начинаем с цели
        String cur = goalX + "_" + goalY;
        // пока не дойдём до старта
        while (!cur.equals(startX + "_" + startY)) {
            String[] parts = cur.split("_");
            int px = Integer.parseInt(parts[0]);
            int py = Integer.parseInt(parts[1]);
            path.add(new Edge(px, py));

            // родитель
            cur = parent.get(cur);
        }
        // Добавляем старт
        path.add(new Edge(startX, startY));

        // Путь построен от конца к началу — разворачиваем
        Collections.reverse(path);

        return path;
    }
}
