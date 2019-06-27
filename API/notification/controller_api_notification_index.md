## Controller API(notification) index

**Version 1.0**
**Copyright(c) 2018 Nippon Telegraph and Telephone Corporation**

|group|notification|interface name|Identification ID|Method|URI|URI example|
|:----|:----|:----|:----|:----|:----|:----|
|Common|Processing result|operation result|000101|PUT|/v1/operations/{operation_id}|/v1/operations/1234567890123|
||controller status|status notification|000201|PUT|/v1/MSFcontroller/status|/v1/MSFcontroller/status|
|Traffic information|Traffic information|traffic notification|010101|PUT|/v1/MSFcontroller/traffic|/v1/MSFcontroller/traffic|
|Failure detection|Failure information|failure notification|020101|PUT|/v1/MSFcontroller/failure_status|/v1/MSFcontroller/failure_status|
|Silent failure detection|Silent failure detection|Silent failure detection|040101|PUT|/v1/MSFcontroller/silent_failure|/v1/MSFcontroller/silent_failure|
|Switch OS upgrade|Switch OS upgrade|Switch OS upgrade notification|050101|PUT|/v1/MSFcontroller/upgrade_operations|/v1/MSFcontroller/upgrade_operations|
|Controller status|Controller error|Controller error notification|060101|PUT|/v1/MSFcontroller/controller_status_notification/failure|/v1/MSFcontroller/controller_status_notification/failure|
||Controller log|Controller log notification|060201|PUT|/v1/MSFcontroller/controller_status_notification/log|/v1/MSFcontroller/controller_status_notification/log|

