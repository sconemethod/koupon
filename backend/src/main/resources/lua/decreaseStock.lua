-- Redis에 있는 stock key를 1 감소시키고
-- 결과가 0 이상이면 1 반환 (성공)
-- 아니면 0 반환 (재고 없음)

if redis.call("decr", KEYS[1]) >= 0 then
  return 1
else
  return 0
end
