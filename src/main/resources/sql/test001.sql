DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `table01_save`(IN `arg_val01` varchar(255))
BEGIN
      INSERT table01 VALUES(arg_val01);

  END
;;
DELIMITER ;