#!/usr/bin/env powershell

# Script de Testes - RabbitMQ (Windows PowerShell)
# Todos os endpoints disponíveis para teste

$BASE_URL = "http://localhost:8080"

Write-Host "================================" -ForegroundColor Cyan
Write-Host "Testando Health Check" -ForegroundColor Cyan
Write-Host "================================" -ForegroundColor Cyan
$response = Invoke-RestMethod -Uri "$BASE_URL/health" -Method Get
Write-Host $response -ForegroundColor Green
Write-Host ""

Write-Host "================================" -ForegroundColor Cyan
Write-Host "Informações de Endpoints" -ForegroundColor Cyan
Write-Host "================================" -ForegroundColor Cyan
$response = Invoke-RestMethod -Uri "$BASE_URL/api/v1/info" -Method Get
Write-Host $response -ForegroundColor Green
Write-Host ""

# ====== DEFAULT EXCHANGE ======
Write-Host "`n`n================================" -ForegroundColor Yellow
Write-Host "1. Testando DEFAULT EXCHANGE" -ForegroundColor Yellow
Write-Host "================================" -ForegroundColor Yellow

$body = @{message = "Teste do Direct Exchange - Mensagem 1"} | ConvertTo-Json
$response = Invoke-RestMethod -Uri "$BASE_URL/api/v1/publisher/default" `
  -Method Post `
  -Headers @{"Content-Type" = "application/json"} `
  -Body $body
Write-Host $response -ForegroundColor Green
Start-Sleep -Seconds 1

Write-Host "`nEnviando mais uma mensagem DEFAULT..." -ForegroundColor White
$body = @{message = "Teste do Direct Exchange - Mensagem 2"} | ConvertTo-Json
$response = Invoke-RestMethod -Uri "$BASE_URL/api/v1/publisher/default" `
  -Method Post `
  -Headers @{"Content-Type" = "application/json"} `
  -Body $body
Write-Host $response -ForegroundColor Green
Start-Sleep -Seconds 1

# ====== FANOUT EXCHANGE ======
Write-Host "`n`n================================" -ForegroundColor Yellow
Write-Host "2. Testando FANOUT EXCHANGE" -ForegroundColor Yellow
Write-Host "================================" -ForegroundColor Yellow
Write-Host "Enviando mensagem para TODAS as filas fanout..." -ForegroundColor White

$body = @{message = "Broadcast Fanout - vai para fanout-queue-1 E fanout-queue-2 E fanout-queue-3"} | ConvertTo-Json
$response = Invoke-RestMethod -Uri "$BASE_URL/api/v1/publisher/fanout" `
  -Method Post `
  -Headers @{"Content-Type" = "application/json"} `
  -Body $body
Write-Host $response -ForegroundColor Green
Start-Sleep -Seconds 1

Write-Host "`nEnviando mais uma mensagem Fanout..." -ForegroundColor White
$body = @{message = "Segundo broadcast para todas as filas"} | ConvertTo-Json
$response = Invoke-RestMethod -Uri "$BASE_URL/api/v1/publisher/fanout" `
  -Method Post `
  -Headers @{"Content-Type" = "application/json"} `
  -Body $body
Write-Host $response -ForegroundColor Green
Start-Sleep -Seconds 1

# ====== TOPIC EXCHANGE - ORDERS ======
Write-Host "`n`n================================" -ForegroundColor Yellow
Write-Host "3. Testando TOPIC EXCHANGE - ORDERS" -ForegroundColor Yellow
Write-Host "================================" -ForegroundColor Yellow

Write-Host "`nEnviando orders.created..." -ForegroundColor White
$body = @{message = "Novo pedido criado"; routingKey = "created"} | ConvertTo-Json
$response = Invoke-RestMethod -Uri "$BASE_URL/api/v1/publisher/topic/orders" `
  -Method Post `
  -Headers @{"Content-Type" = "application/json"} `
  -Body $body
Write-Host $response -ForegroundColor Green
Start-Sleep -Seconds 1

Write-Host "`nEnviando orders.updated..." -ForegroundColor White
$body = @{message = "Pedido foi atualizado"; routingKey = "updated"} | ConvertTo-Json
$response = Invoke-RestMethod -Uri "$BASE_URL/api/v1/publisher/topic/orders" `
  -Method Post `
  -Headers @{"Content-Type" = "application/json"} `
  -Body $body
Write-Host $response -ForegroundColor Green
Start-Sleep -Seconds 1

Write-Host "`nEnviando orders.deleted..." -ForegroundColor White
$body = @{message = "Pedido foi cancelado"; routingKey = "deleted"} | ConvertTo-Json
$response = Invoke-RestMethod -Uri "$BASE_URL/api/v1/publisher/topic/orders" `
  -Method Post `
  -Headers @{"Content-Type" = "application/json"} `
  -Body $body
Write-Host $response -ForegroundColor Green
Start-Sleep -Seconds 1

# ====== TOPIC EXCHANGE - NOTIFICATIONS ======
Write-Host "`n`n================================" -ForegroundColor Yellow
Write-Host "4. Testando TOPIC EXCHANGE - NOTIFICATIONS" -ForegroundColor Yellow
Write-Host "================================" -ForegroundColor Yellow

Write-Host "`nEnviando notifications.email..." -ForegroundColor White
$body = @{message = "Enviar notificação por email"; routingKey = "email"} | ConvertTo-Json
$response = Invoke-RestMethod -Uri "$BASE_URL/api/v1/publisher/topic/notifications" `
  -Method Post `
  -Headers @{"Content-Type" = "application/json"} `
  -Body $body
Write-Host $response -ForegroundColor Green
Start-Sleep -Seconds 1

Write-Host "`nEnviando notifications.sms..." -ForegroundColor White
$body = @{message = "Enviar notificação por SMS"; routingKey = "sms"} | ConvertTo-Json
$response = Invoke-RestMethod -Uri "$BASE_URL/api/v1/publisher/topic/notifications" `
  -Method Post `
  -Headers @{"Content-Type" = "application/json"} `
  -Body $body
Write-Host $response -ForegroundColor Green
Start-Sleep -Seconds 1

Write-Host "`nEnviando notifications.push..." -ForegroundColor White
$body = @{message = "Enviar notificação Push"; routingKey = "push"} | ConvertTo-Json
$response = Invoke-RestMethod -Uri "$BASE_URL/api/v1/publisher/topic/notifications" `
  -Method Post `
  -Headers @{"Content-Type" = "application/json"} `
  -Body $body
Write-Host $response -ForegroundColor Green
Start-Sleep -Seconds 1

Write-Host "`n`n================================" -ForegroundColor Green
Write-Host "✅ Testes Completados!" -ForegroundColor Green
Write-Host "================================" -ForegroundColor Green
Write-Host "Verifique os logs da aplicação para ver os consumers processando as mensagens" -ForegroundColor White
