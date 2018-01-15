## Controller API(notification) index

**Version 1.0**
**Copyright(c) 2017 Nippon Telegraph and Telephone Corporation**

|group|notification|interface name|Identification ID|Method|URI|URI example|
|:----|:----|:----|:----|:----|:----|:----|
|Common|Processing result|operation result|000101|PUT|/v1/operations/{operation_id}|/v1/operations/1234567890123|
||controller status|status notification|000201|PUT|/v1/MSFcontroller/status|/v1/MSFcontroller/status|
|Traffic information|Traffic information|traffic notification|010101|PUT|/v1/MSFcontroller/traffic|/v1/MSFcontroller/traffic|
|Failure detection|Failure information|failure notification|020101|PUT|/v1/MSFcontroller/failure_status|/v1/MSFcontroller/failure_status|
