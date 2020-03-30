nohup java \
  -server \
  -Xmx3G \
  -Xms3G \
  -Xmn1G \
  -XX:+DisableExplicitGC \
  -XX:SurvivorRatio=1 \
  -XX:+UseConcMarkSweepGC \
  -XX:+CMSParallelRemarkEnabled \
  -XX:+UseCMSCompactAtFullCollection \
  -XX:CMSMaxAbortablePrecleanTime=500 \
  -XX:+CMSClassUnloadingEnabled \
  -jar ./gw-api-1.0.jar >log 2>&1 &
echo server has started!
