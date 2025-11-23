$BASE_URL = "http://localhost:8080"

Write-Host "Enviando notifications.sms..." -ForegroundColor White
$body = @{message = "Mensagem de teste RabbitMQ com Confirm e Return - v2"} | ConvertTo-Json
$response = Invoke-RestMethod -Uri "$BASE_URL/api/v1/publisher" `
  -Method Post `
  -Headers @{"Content-Type" = "application/json"} `
  -Body $body
Write-Host $response -ForegroundColor Green
Start-Sleep -Seconds 1