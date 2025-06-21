@echo off
echo Запуск тестов и генерация отчетов...
call mvn clean test surefire-report:report

echo.
echo Отчеты сгенерированы в папке test-results:
echo   - test-summary.txt - краткая сводка
echo   - test-report.html - HTML отчет с деталями
echo   - full-console-output.txt - полный консольный вывод
echo.
echo Открываю папку с отчетами...
start "" "test-results"
pause