package ServiceLayer;

public class Service {


        private  static Service service= null;

        public static Service getInstance() {
            if (service == null)
                service = new Service();
            return service;
        }







}
