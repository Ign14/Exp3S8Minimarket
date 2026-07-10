@echo off
chcp 65001 >nul
cd /d "%~dp0"
echo ============================================================
echo   Subir MiniMarket Plus (Exp3 S8) a GitHub
echo ============================================================
echo Antes crea el repo VACIO en https://github.com/new
echo   Nombre sugerido: Exp3S8Minimarket  (Publico, sin README)
echo ------------------------------------------------------------
set /p REPO="Pega la URL del repo (ej: https://github.com/Ign14/Exp3S8Minimarket.git): "
echo.
git init
git add .
git commit -m "Exp3 S8: documentacion OpenAPI + HATEOAS sobre MiniMarket Plus"
git branch -M main
git remote add origin %REPO%
git push -u origin main
echo.
echo Listo. Revisa el repositorio en tu navegador.
pause
