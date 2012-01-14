ps aux | grep timetracker | awk '{print $2}' | xargs kill -9
TIMELOG_HOME=/space/home/ilya/safeplace/timelog
TIMELOG_REMOTE_HOME=timelog/timeDB
TIMELOG_REMOTE_HOST=beavermobile
rm -r $TIMELOG_HOME/timeDB.bak
mv $TIMELOG_HOME/timeDB $TIMELOG_HOME/timeDB.bak
ssh $TIMELOG_REMOTE_HOST "ps aux | grep timetracker | awk '{print \$2}'| xargs kill -9"
scp -r $TIMELOG_REMOTE_HOST:$TIMELOG_REMOTE_HOME $TIMELOG_HOME
